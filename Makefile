STUID = ysyx_22040200
STUNAME = 乌鑫龙

NVBOARD_HOME = $(shell pwd)/nvboard
INC_PATH ?=
NXDC_FILES = npc/constr/top.nxdc

TOPNAME = top
NPC_DIR = $(shell pwd)/npc
CPP_DIR = $(NPC_DIR)/csrc
VERILOG_DIR = $(NPC_DIR)/vsrc
BUILD_DIR = $(shell pwd)/build
CHISEL_BUILD = $(NPC_DIR)/build
WAVE_FILE = $(BUILD_DIR)/top.vcd
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
CPP_SRC += $(SRC_AUTO_BIND)

# DO NOT modify the following code!!!
GITFLAGS = -q --author='tracer-ysyx2204 <tracer@ysyx.org>' --no-verify --allow-empty

# prototype: git_commit(msg)
define git_commit
	-@git add . -A --ignore-errors
	-@while (test -e .git/index.lock); do sleep 0.1; done
	-@(echo "> $(1)" && echo $(STUID) $(STUNAME) && uname -a && uptime) | git commit -F - $(GITFLAGS)
	-@sync
endef

# rules for NVBoard
include $(NVBOARD_HOME)/scripts/nvboard.mk

INCFLAGS = $(addprefix -I, $(INC_PATH))
CFLAGS += $(INCFLAGS) -DTOP_NAME="\"V$(TOPNAME)\""
VERILATOR_CFLAGS += -MMD --build -cc --trace  \
				-O3 --x-assign fast --x-initial fast --noassert
LDFLAGS += -lSDL2 -lSDL2_image

sim: build
	$(call git_commit, "sim RTL") # DO NOT REMOVE THIS LINE!!!
	
nvboard: build
	@$(BUILD_DIR)/$(TOPNAME)

gtkwave: build
	@$(BUILD_DIR)/$(TOPNAME);gtkwave $(WAVE_FILE)

build: verilog $(VERILOG_SRC) $(CPP_SRC) $(NVBOARD_ARCHIVE)
	@rm -rf $(BUILD_DIR)/obj_dir
	verilator $(VERILATOR_CFLAGS) -j $(JOB_NUM) \
		-top $(TOPNAME) $(VERILOG_SRC) $(CPP_SRC) $(NVBOARD_ARCHIVE) \
		$(addprefix -CFLAGS , $(CFLAGS)) $(addprefix -LDFLAGS , $(LDFLAGS)) \
		--Mdir $(BUILD_DIR)/obj_dir -o $(abspath $(BUILD_DIR)/$(TOPNAME))

clean: cleanchisel
	rm -rf $(BUILD_DIR)

.PHONY: clean

include npc/Makefile



