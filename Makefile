STUID = ysyx_22040000
STUNAME = 张三

CPP_DIR = npc/csrc/
VERILOG_DIR = npc/vsrc/
BUILD_DIR = build/
MAX_THREAD = `cat /proc/cpuinfo |grep "processor"|wc -l`
JOB_NUM = $(shell expr $(MAX_THREAD) - 1)

OBJ_SRC = $(basename $(notdir $(wildcard $(VERILOG_DIR)*.v)))

VERILOG_SRC = $(wildcard $(VERILOG_DIR)*.v)
CPP_SRC = $(wildcard $(CPP_DIR)*.cpp)

# DO NOT modify the following code!!!

GITFLAGS = -q --author='tracer-ysyx2204 <tracer@ysyx.org>' --no-verify --allow-empty

# prototype: git_commit(msg)
define git_commit
	-@git add .. -A --ignore-errors
	-@while (test -e .git/index.lock); do sleep 0.1; done
	-@(echo "> $(1)" && echo $(STUID) $(STUNAME) && uname -a && uptime) | git commit -F - $(GITFLAGS)
	-@sync
endef

_default:	build
