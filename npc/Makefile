SUB_DIR = $(shell pwd)/npc
test:
	@cd $(SUB_DIR); mill -i __.test

verilog:
	$(call git_commit, "generate verilog")
	mkdir -p $(VERILOG_DIR)
	cd $(SUB_DIR); mill -i __.test.runMain Elaborate -td ./build

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
	# -rm -rf $(VERILOG_DIR)

.PHONY: test verilog help compile bsp reformat checkformat cleanchisel
