module top (
    input clock,
    input reset,
    input [15:0] sw,
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

chiseltop chiseltop(
    .clock(clock),
    .reset(reset),
    .io_sw(sw),
    .io_ps2_clk(ps2_clk),
    .io_ps2_data(ps2_data),
    .io_ledr(ledr),
    .io_VGA_CLK(VGA_CLK),
    .io_VGA_HSYNC(VGA_HSYNC),
    .io_VGA_VSYNC(VGA_VSYNC),
    .io_VGA_BLANK_N(VGA_BLANK_N),
    .io_VGA_R(VGA_R),
    .io_VGA_G(VGA_G),
    .io_VGA_B(VGA_B),
    .io_seg0(seg0),
    .io_seg1(seg1),
    .io_seg2(seg2),
    .io_seg3(seg3),
    .io_seg4(seg4),
    .io_seg5(seg5),
    .io_seg6(seg6),
    .io_seg7(seg7)
);

assign VGA_CLK = clock;

wire [9:0] h_addr;
wire [9:0] v_addr;
wire [23:0] vga_data;

vga_ctrl my_vga_ctrl(
    .pclk(clock),
    .reset(reset),
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
    .clk(clock),
    .resetn(~reset),
    .ps2_clk(ps2_clk),
    .ps2_data(ps2_data)
);

vmem my_vmem(
    .clock(clock)
    .reset(reset)
    .io_h_addr(h_addr),
    .io_v_addr(v_addr[8:0]),
    .io_vga_data(vga_data)
);

endmodule

// module vmem (
//     input [9:0] h_addr,
//     input [8:0] v_addr,
//     output [23:0] vga_data
// );

// reg [23:0] vga_mem [0:524287];

// initial begin
//     $readmemh("npc/resource/picture.hex", vga_mem);
// end

// assign vga_data = vga_mem[{h_addr, v_addr}];

// endmodule
