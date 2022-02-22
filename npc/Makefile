SUB_DIR = `pwd`/npc
test:
	@cd $(SUB_DIR)
	mill -i __.test

verilog:
	$(call git_commit, "generate verilog")
	@cd $(SUB_DIR)
	mkdir -p $(SUB_DIR)/$(BUILD_DIR)
	mill -i __.test.runMain Elaborate -td $(SUB_DIR)/$(BUILD_DIR)

help:
	@cd $(SUB_DIR)
	mill -i __.test.runMain Elaborate --help

compile:
	@cd $(SUB_DIR)
	mill -i __.compile

bsp:
	@cd $(SUB_DIR)
	mill -i mill.bsp.BSP/install

reformat:
	@cd $(SUB_DIR)
	mill -i __.reformat

checkformat:
	@cd $(SUB_DIR)
	mill -i __.checkFormat

cleanchisel:
	-rm -rf $(SUB_DIR)/$(BUILD_DIR)

.PHONY: test verilog help compile bsp reformat checkformat cleanchisel

sim:
	$(call git_commit, "sim RTL") # DO NOT REMOVE THIS LINE!!!
	@echo "Write this Makefile by yourself."
