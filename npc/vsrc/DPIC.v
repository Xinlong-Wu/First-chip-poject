module DPIC(
    input is_ebreak
);
    import "DPI-C" function void hit_ebreak();
    always begin
        if(is_ebreak) begin
            hit_ebreak();
        end
    end
endmodule