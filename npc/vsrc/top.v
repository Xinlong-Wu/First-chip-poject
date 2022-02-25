module top (
    input clk,
    input rst,
    input [9:0] sw,
    input ps2_clk,
    input ps2_data,
    output [15:0] ledr,
    output VGA_CLK,
    output VGA_HSYNC,
    output VGA_VSYNC,
    output VGA_BLANK_N,
    output [7:0] VGA_R,
    output [7:0] VGA_G,
    output [7:0] VGA_B,
    output [7:0] seg0,
    output [7:0] seg1,
    output [7:0] seg2,
    output [7:0] seg3,
    output [7:0] seg4,
    output [7:0] seg5,
    output [7:0] seg6,
    output [7:0] seg7
);

assign seg0 = 8'b11111111;
assign seg1 = 8'b11111111;
assign seg2 = 8'b11111111;
assign seg3 = 8'b11111111;
assign seg4 = 8'b11111111;
assign seg5 = 8'b11111111;
assign seg6 = 8'b11111111;
assign seg7 = 8'b11111111;

light light(
    .clk(clk),
    .rst(rst),
    .led(ledr)
);

wire [2:0] res;
encoder83 encoder83(
    .clk(clk),
    .rst(rst),
    .io_x(sw[7:0]),
    .io_en(sw[8]),
    .io_y(res)
)

bcd7seg bcd7seg(
    .clk(clk),
    .rst(rst),
    .io_num({1'b0, res})
    .io_en(sw[9]),
    .io_HEX(seg0)
)

assign VGA_CLK = clk;

wire [9:0] h_addr;
wire [9:0] v_addr;
wire [23:0] vga_data;

vga_ctrl my_vga_ctrl(
    .pclk(clk),
    .reset(rst),
    .vga_data(vga_data),
    .h_addr(h_addr),
    .v_addr(v_addr),
    .hsync(VGA_HSYNC),
    .vsync(VGA_VSYNC),
    .valid(VGA_BLANK_N),
    .vga_r(VGA_R),
    .vga_g(VGA_G),
    .vga_b(VGA_B)
);

ps2_keyboard my_keyboard(
    .clk(clk),
    .resetn(~rst),
    .ps2_clk(ps2_clk),
    .ps2_data(ps2_data)
);

vmem my_vmem(
    .h_addr(h_addr),
    .v_addr(v_addr[8:0]),
    .vga_data(vga_data)
);

endmodule

module vmem (
    input [9:0] h_addr,
    input [8:0] v_addr,
    output [23:0] vga_data
);

reg [23:0] vga_mem [524287:0];

initial begin
    $readmemh("npc/resource/picture.hex", vga_mem);
end

assign vga_data = vga_mem[{h_addr, v_addr}];

endmodule
