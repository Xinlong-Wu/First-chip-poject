module top (
    input clk,
    input rst,
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
    .led(ledr[15:4])
);

wire [24:0] count_clk = 0;
wire clk_1s = 0;

always @(posedge clk)
  if(count_clk==24999999)
  begin
    count_clk <=0;
    clk_1s <= ~clk_1s;
  end
  else
    count_clk <= count_clk+1;

wire [7:0] count = 0;

Counter Coounter(
    .clock(clk_1s),
    .reset(rst),
    .io_en(1),
    .io_out(count)
);

bcd7seg bcd7seg_1(
    .clock(clk),
    .reset(rst),
    .io_num((count%10)[4:0]),
    .io_en(1),
    .io_HEX(seg0)
);

bcd7seg bcd7seg_2(
    .clock(clk),
    .reset(rst),
    .io_num(((count%100)/10)[4:0]),
    .io_en(1),
    .io_HEX(seg1)
);

bcd7seg bcd7seg_3(
    .clock(clk),
    .reset(rst),
    .io_num((count/100)[4:0]),
    .io_en(1),
    .io_HEX(seg2)
);

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
