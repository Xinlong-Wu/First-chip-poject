module DPIC(
    input is_ebreak,
    input [31:0] result,
    input unimp
);
    import "DPI-C" function void hit_ebreak(int res, bit unimp);
    always begin
        if(is_ebreak) begin
            hit_ebreak(result,unimp);
        end
    end
endmodule