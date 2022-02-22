build: $(VERILOG_SRC) $(CPP_SRC)
	@echo "$(JOB_NUM)"
	@verilator -j $(JOB_NUM) --cc --exe --trace --build $(VERILOG_SRC) $(CPP_SRC) --Mdir $(BUILD_DIR)

sim:
	$(call git_commit, "sim RTL") # DO NOT REMOVE THIS LINE!!!
	@echo "Write this Makefile by your self."
