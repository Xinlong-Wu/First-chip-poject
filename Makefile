STUID = ysyx_22040000
STUNAME = 张三

TOP_MOD = GCD.v
NPC_DIR = $(shell pwd)/npc
CPP_DIR = $(NPC_DIR)/csrc
VERILOG_DIR = $(NPC_DIR)/vsrc
BUILD_DIR = $(shell pwd)/build
WAVE_FILE = $(BUILD_DIR)/wave/top.vcd

MAX_THREAD = `cat /proc/cpuinfo |grep "processor"|wc -l`
JOB_NUM = $(shell expr $(MAX_THREAD) - 1)

OBJ_SRC = $(basename $(notdir $(wildcard $(VERILOG_DIR)/*.v)))

VERILOG_SRC = $(wildcard $(VERILOG_DIR)/*.v)
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
	
sim: build
	# $(call git_commit, "sim RTL") # DO NOT REMOVE THIS LINE!!!
	$(shell exec $(BUILD_DIR)/V$(basename $(TOP_MOD)))
	gtkwave $(TOP_MOD)

header:
	echo "" > $(CPP_DIR)/TEMP.h
	$(foreach f,$(OBJ_SRC), echo '#include"V$(f).h"' >> /$(CPP_DIR)/TEMP.h)
	echo "#define Vtop V$(firstword $(OBJ_SRC))" >> $(CPP_DIR)/TEMP.h
	echo "#define WAVE_FILE $(WAVE_FILE)" >> $(CPP_DIR)/TEMP.h

build: verilog header $(VERILOG_SRC) $(CPP_SRC)
	verilator -j $(JOB_NUM) --cc --exe --trace --build $(VERILOG_SRC) $(CPP_SRC) --Mdir $(BUILD_DIR)
	
.clean_index:
	rm -f $(WORK_INDEX)

_default:	build

.PHONY: .git_commit .clean_index _default

include npc/Makefile
