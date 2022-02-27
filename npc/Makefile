YSYX_HOME=/home/vincent/CodeSpace/First-chip-poject
NPC_DIR = $(YSYX_HOME)/npc
CHISEL_BUILD = $(NPC_DIR)/build
CHISEL_DIR = $(NPC_DIR)/chisel/src
CHISEL_SRC = $(wildcard $(CHISEL_DIR)/*.scala)
test:
	mill -i __.test

verilog: $(CHISEL_SRC)
	$(call git_commit, "generate verilog")
	python $(NPC_DIR)/resource/AsciiGen.py $(NPC_DIR)/resource
	mill -i __.test.runMain Elaborate -td $(CHISEL_BUILD)

help:
	mill -i __.test.runMain Elaborate --help

compile:
	mill -i __.compile

bsp:
	mill -i mill.bsp.BSP/install

reformat:
	mill -i __.reformat

checkformat:
	mill -i __.checkFormat

cleanchisel:
	rm -rf $(CHISEL_BUILD)
	rm -rf $(NPC_DIR)/out

.PHONY: test help compile bsp reformat checkformat cleanchisel
