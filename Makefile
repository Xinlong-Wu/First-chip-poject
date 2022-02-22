STUID = ysyx_22040000
STUNAME = 张三

TOP_MOD = GCD.v
NPC_DIR = $(shell pwd)/npc
CPP_DIR = $(NPC_DIR)/csrc
VERILOG_DIR = $(NPC_DIR)/vsrc
BUILD_DIR = $(shell pwd)/build
WAVE_FILE = $(BUILD_DIR)/top.vcd

MAX_THREAD = `cat /proc/cpuinfo |grep "processor"|wc -l`
JOB_NUM = $(shell expr $(MAX_THREAD) - 1)

OBJ_SRC = $(basename $(notdir $(wildcard $(VERILOG_DIR)/*.v)))

VERILOG_SRC = $(wildcard $(VERILOG_DIR)/*.v)
CPP_SRC = $(wildcard $(CPP_DIR)/*.cpp)

# DO NOT modify the following code!!!

GITFLAGS = -q --author='tracer-ysyx2204 <tracer@ysyx.org>' --no-verify --allow-empty

# prototype: git_commit(msg)
define git_commit
	-@git add . -A --ignore-errors
	-@while (test -e .git/index.lock); do sleep 0.1; done
	-@(echo "> $(1)" && echo $(STUID) $(STUNAME) && uname -a && uptime) | git commit -F - $(GITFLAGS)
	-@sync
endef

sim: build
	$(call git_commit, "sim RTL") # DO NOT REMOVE THIS LINE!!!
	@$(BUILD_DIR)/V$(basename $(TOP_MOD));gtkwave $(WAVE_FILE)

header:
	echo "" > $(CPP_DIR)/TEMP.h
	$(foreach f,$(OBJ_SRC), echo '#include"V$(f).h"' >> /$(CPP_DIR)/TEMP.h)
	echo '#define WAVE_FILE "$(WAVE_FILE)"' >> $(CPP_DIR)/TEMP.h

build: verilog header $(VERILOG_SRC) $(CPP_SRC)
	verilator -j $(JOB_NUM) --cc --exe --trace --build $(VERILOG_SRC) $(CPP_SRC) --Mdir $(BUILD_DIR)

clean: cleanchisel
	rm -rf $(BUILD_DIR)

.PHONY: clean

include npc/Makefile



