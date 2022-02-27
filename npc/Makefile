YSYX_HOME=/home/vincent/CodeSpace/First-chip-poject
NPC_DIR = $(YSYX_HOME)/npc
CHISEL_DIR = $(NPC_DIR)/chisel/src
CHISEL_SRC = $(wildcard $(CHISEL_DIR)/*.scala)
test:
	@cd $(NPC_DIR); mill -i __.test

verilog: $(CHISEL_SRC)
	$(call git_commit, "generate verilog")
	cd $(NPC_DIR); mill -i __.test.runMain Elaborate -td $(CHISEL_BUILD)

help:
	@cd $(SUB_DIR); mill -i __.test.runMain Elaborate --help

compile:
	@cd $(SUB_DIR); mill -i __.compile

bsp:
	@cd $(SUB_DIR); mill -i mill.bsp.BSP/install

reformat:
	@cd $(SUB_DIR); mill -i __.reformat

checkformat:
	@cd $(SUB_DIR); mill -i __.checkFormat

cleanchisel:
	rm -rf $(CHISEL_BUILD)
	rm -rf $(SUB_DIR)/out

.PHONY: test verilog help compile bsp reformat checkformat cleanchisel
