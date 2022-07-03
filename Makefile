STUID = ysyx_22040200
STUNAME = 乌鑫龙

YSYX_HOME=/home/vincent/CodeSpace/First-chip-poject

INC_PATH ?=
NXDC_FILES = $(YSYX_HOME)/npc/constr/top.nxdc

TOPNAME = top
NPC_DIR = $(YSYX_HOME)/npc
CPP_DIR = $(NPC_DIR)/csrc
CHISEL_DIR = $(NPC_DIR)/chisel/src
VERILOG_DIR = $(NPC_DIR)/vsrc
BUILD_DIR = $(YSYX_HOME)/build
CHISEL_BUILD = $(NPC_DIR)/build
WAVE_FILE = $(CHISEL_BUILD)/top.vcd
$(shell mkdir -p $(BUILD_DIR))

MAX_THREAD = `cat /proc/cpuinfo |grep "processor"|wc -l`
JOB_NUM = $(shell expr $(MAX_THREAD) - 1)

SRC_AUTO_BIND = $(abspath $(BUILD_DIR)/auto_bind.cpp)
$(SRC_AUTO_BIND): $(NXDC_FILES)
	python $(NVBOARD_HOME)/scripts/auto_pin_bind.py $^ $@

OBJ_SRC = $(basename $(notdir $(wildcard $(VERILOG_DIR)/*.v)))

VERILOG_SRC = $(wildcard $(VERILOG_DIR)/*.v)
VERILOG_SRC += $(wildcard $(CHISEL_BUILD)/*.v)
CPP_SRC = $(wildcard $(CPP_DIR)/*.cpp)

# DO NOT modify the following code!!!

TRACER = tracer-ysyx2204
GITFLAGS = -q --author='$(TRACER) <tracer@ysyx.org>' --no-verify --allow-empty

YSYX_HOME = $(NEMU_HOME)/..
WORK_BRANCH = $(shell git rev-parse --abbrev-ref HEAD)
WORK_INDEX = $(YSYX_HOME)/.git/index.$(WORK_BRANCH)
TRACER_BRANCH = $(TRACER)

LOCK_DIR = $(YSYX_HOME)/.git/

# prototype: git_soft_checkout(branch)
define git_soft_checkout
	git checkout --detach -q && git reset --soft $(1) -q && git checkout $(1) -q
endef

# prototype: git_commit(msg)
define git_commit
	-@flock $(LOCK_DIR) $(MAKE) .git_commit MSG='$(1)'
	-@sync
endef

.git_commit:
	-@cd $(YSYX_HOME) && while (test -e .git/index.lock); do sleep 0.1; done;               `# wait for other git instances`
	-@cd $(YSYX_HOME) && git branch $(TRACER_BRANCH) -q 2>/dev/null || true                 `# create tracer branch if not existent`
	-@cd $(YSYX_HOME) && cp -a .git/index $(WORK_INDEX)                                     `# backup git index`
	-@cd $(YSYX_HOME) && $(call git_soft_checkout, $(TRACER_BRANCH))                        `# switch to tracer branch`
	-@cd $(YSYX_HOME) && git add . -A --ignore-errors                                       `# add files to commit`
	-@cd $(YSYX_HOME) && (echo "> $(MSG)" && echo $(STUID) $(STUNAME) && uname -a && uptime `# generate commit msg`) \
	                | git commit -F - $(GITFLAGS)                                           `# commit changes in tracer branch`
	-@cd $(YSYX_HOME) && $(call git_soft_checkout, $(WORK_BRANCH))                          `# switch to work branch`
	-@cd $(YSYX_HOME) && mv $(WORK_INDEX) .git/index                                        `# restore git index`
	
# rules for NVBoard
include $(NVBOARD_HOME)/scripts/nvboard.mk

INCFLAGS = $(addprefix -I, $(INC_PATH))
NPC_CFLAGS += -g $(INCFLAGS) -DTOP_NAME="\"V$(TOPNAME)\""
VERILATOR_CFLAGS += -MMD --build -cc --trace  \
				-O3 --x-assign fast --x-initial fast --noassert --exe
NPC_LDFLAGS += -lSDL2 -lSDL2_image -lreadline

IMAGE_PATH ?= inst.bin

sim: verilog $(VERILOG_SRC) $(CPP_SRC)
	$(call git_commit, "sim RTL") # DO NOT REMOVE THIS LINE!!!
	verilator $(VERILATOR_CFLAGS) -j $(JOB_NUM) \
		-top $(TOPNAME) $(VERILOG_SRC) $(CPP_SRC) \
		$(addprefix -CFLAGS , $(NPC_CFLAGS)) $(addprefix -LDFLAGS , $(NPC_LDFLAGS)) \
		--Mdir $(BUILD_DIR)/obj_dir -o $(abspath $(BUILD_DIR)/$(TOPNAME))
	$(abspath $(BUILD_DIR)/$(TOPNAME)) ${IMAGE_PATH}

gdb: verilog $(VERILOG_SRC) $(CPP_SRC)
	$(call git_commit, "sim RTL") # DO NOT REMOVE THIS LINE!!!
	verilator $(VERILATOR_CFLAGS) -j $(JOB_NUM) \
		-top $(TOPNAME) $(VERILOG_SRC) $(CPP_SRC) \
		$(addprefix -CFLAGS , $(NPC_CFLAGS)) $(addprefix -LDFLAGS , $(NPC_LDFLAGS)) \
		--Mdir $(BUILD_DIR)/obj_dir -o $(abspath $(BUILD_DIR)/$(TOPNAME))
	gdb --args $(abspath $(BUILD_DIR)/$(TOPNAME)) ${IMAGE_PATH}
	
nvboard: build
	@$(BUILD_DIR)/$(TOPNAME)

nvboard-debug: build
	@$(BUILD_DIR)/$(TOPNAME) &> $(BUILD_DIR)/log


gtkwave: sim
	$(BUILD_DIR)/$(TOPNAME);gtkwave $(WAVE_FILE)

build: verilog $(VERILOG_SRC) $(CPP_SRC) $(NVBOARD_ARCHIVE)
	@rm -rf $(BUILD_DIR)/obj_dir
	python $(NPC_DIR)/resource/AsciiGen.py mask $(NPC_DIR)/resource
	verilator $(VERILATOR_CFLAGS) -j $(JOB_NUM) \
		-top $(TOPNAME) $(VERILOG_SRC) $(CPP_SRC) $(SRC_AUTO_BIND) $(NVBOARD_ARCHIVE) \
		$(addprefix -CFLAGS , $(NPC_CFLAGS)) $(addprefix -LDFLAGS , $(NPC_LDFLAGS)) \
		--Mdir $(BUILD_DIR)/obj_dir -o $(abspath $(BUILD_DIR)/$(TOPNAME))

_default:	build

cleanall: cleanchisel
	rm -rf $(BUILD_DIR)

.PHONY: cleanall .git_commit .clean_index _default

include $(YSYX_HOME)/npc/Makefile



