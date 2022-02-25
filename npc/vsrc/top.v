module top (
    input clock,
    input reset,
    input [15:0] io_sw,
    input io_ps2_clk,
    input io_ps2_data,
    output [15:0] io_ledr,
    output io_VGA_CLK,
    output io_VGA_HSYNC,
    output io_VGA_VSYNC,
    output io_VGA_BLANK_N,
    output [7:0] io_VGA_R,
    output [7:0] io_VGA_G,
    output [7:0] io_VGA_B,
    output [7:0] io_seg0,
    output [7:0] io_seg1,
    output [7:0] io_seg2,
    output [7:0] io_seg3,
    output [7:0] io_seg4,
    output [7:0] io_seg5,
    output [7:0] io_seg6,
    output [7:0] io_seg7
);

// assign io_seg0 = 8'b11111111;
// assign io_seg1 = 8'b11111111;
// assign io_seg2 = 8'b11111111;
// assign io_seg3 = 8'b11111111;
// assign io_seg4 = 8'b11111111;
// assign io_seg5 = 8'b11111111;
// assign io_seg6 = 8'b11111111;
// assign io_seg7 = 8'b11111111;

// light light(
//     .clk(clock),
//     .rst(reset),
//     .led(io_ledr[15:4])
// );

// wire [24:0] count_clk = 0;
// wire clk_1s = 0;

// always @(posedge clk)
//   if(count_clk==24999999)
//   begin
//     count_clk <=0;
//     clk_1s <= ~clk_1s;
//   end
//   else
//     count_clk <= count_clk+1;

// wire [7:0] count = 0;

// Counter Coounter(
//     .clock(clk_1s),
//     .reset(rst),
//     .io_en(1),
//     .io_out(count)
// );

// bcd7seg bcd7seg_1(
//     .clock(clk),
//     .reset(rst),
//     .io_num((count%10)[4:0]),
//     .io_en(1),
//     .io_HEX(seg0)
// );

// bcd7seg bcd7seg_2(
//     .clock(clk),
//     .reset(rst),
//     .io_num(((count%100)/10)[4:0]),
//     .io_en(1),
//     .io_HEX(seg1)
// );

// bcd7seg bcd7seg_3(
//     .clock(clk),
//     .reset(rst),
//     .io_num((count/100)[4:0]),
//     .io_en(1),
//     .io_HEX(seg2)
// );

// assign io_VGA_CLK = clock;

// wire [9:0] h_addr;
// wire [9:0] v_addr;
// wire [23:0] vga_data;

// vga_ctrl my_vga_ctrl(
//     .pclk(clock),
//     .reset(reset),
//     .vga_data(vga_data),
//     .h_addr(h_addr),
//     .v_addr(v_addr),
//     .hsync(io_VGA_HSYNC),
//     .vsync(io_VGA_VSYNC),
//     .valid(io_VGA_BLANK_N),
//     .vga_r(io_VGA_R),
//     .vga_g(io_VGA_G),
//     .vga_b(io_VGA_B)
// );

// ps2_keyboard my_keyboard(
//     .clk(clock),
//     .resetn(~reset),
//     .ps2_clk(io_ps2_clk),
//     .ps2_data(io_ps2_data)
// );

// vmem my_vmem(
//     .h_addr(h_addr),
//     .v_addr(v_addr[8:0]),
//     .vga_data(vga_data)
// );

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
