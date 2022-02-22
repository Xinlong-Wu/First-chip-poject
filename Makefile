STUID = ysyx_22040000
STUNAME = 张三

BASE_DIR = .
NPC_DIR = $(BASE_DIR)/npc
CPP_DIR = csrc
VERILOG_DIR = vsrc
BUILD_DIR = build

MAX_THREAD = `cat /proc/cpuinfo |grep "processor"|wc -l`
JOB_NUM = $(shell expr $(MAX_THREAD) - 1)

OBJ_SRC = $(basename $(notdir $(wildcard $(NPC_DIR)/$(VERILOG_DIR)/*.v)))

VERILOG_SRC = $(wildcard $(NPC_DIR)/$(VERILOG_DIR)/*.v)
CPP_SRC = $(wildcard $(NPC_DIR)/$(CPP_DIR)/*.cpp)

# DO NOT modify the following code!!!

GITFLAGS = -q --author='tracer-ysyx2204 <tracer@ysyx.org>' --no-verify --allow-empty

# prototype: git_commit(msg)
define git_commit
	-@git add . -A --ignore-errors
	-@while (test -e .git/index.lock); do sleep 0.1; done
	-@(echo "> $(1)" && echo $(STUID) $(STUNAME) && uname -a && uptime) | git commit -F - $(GITFLAGS)
	-@sync
endef

build: verilog $(VERILOG_SRC) $(CPP_SRC)
	echo "" > $(NPC_DIR)/$(CPP_DIR)/TEMP.h
	$(foreach f,$(OBJ_SRC), echo '#include"V$(f).h"' >> $(NPC_DIR)/$(CPP_DIR)/TEMP.h)
	echo "#define Vtop V$(firstword $(OBJ_SRC))" >> $(NPC_DIR)/$(CPP_DIR)/TEMP.h
	verilator -j $(JOB_NUM) --cc --exe --trace --build $(VERILOG_SRC) $(CPP_SRC) --Mdir $(BUILD_DIR)

clean: cleanchisel
	rm -rf $(BUILD_DIR)

.PHONY: clean

include npc/Makefile



