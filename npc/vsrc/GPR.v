`define ZERO_WORD  64'h00000000_00000000 

module GPR #( DATA_WIDTH = 64) (
  input wire rst,
  input wire clk,
  input wire [DATA_WIDTH-1:0] wdata,
  input wire [4:0] waddr,
  input wire wen,
  input wire [4:0]raddr1,
  input wire ren1,
  input wire [4:0]raddr2,
  input wire ren2,

  output reg [DATA_WIDTH-1:0] rdata1,
  output reg [DATA_WIDTH-1:0] rdata2,
  output wire [DATA_WIDTH-1:0] DPIC_res
);
  reg [DATA_WIDTH-1:0] rf [31:0];
  
  always @(posedge clk) 
	begin
		if ( rst == 0'b1 ) 
		begin
			rf[ 0] <= `ZERO_WORD;
			rf[ 1] <= `ZERO_WORD;
			rf[ 2] <= `ZERO_WORD;
			rf[ 3] <= `ZERO_WORD;
			rf[ 4] <= `ZERO_WORD;
			rf[ 5] <= `ZERO_WORD;
			rf[ 6] <= `ZERO_WORD;
			rf[ 7] <= `ZERO_WORD;
			rf[ 8] <= `ZERO_WORD;
			rf[ 9] <= `ZERO_WORD;
			rf[10] <= `ZERO_WORD;
			rf[11] <= `ZERO_WORD;
			rf[12] <= `ZERO_WORD;
			rf[13] <= `ZERO_WORD;
			rf[14] <= `ZERO_WORD;
			rf[15] <= `ZERO_WORD;
			rf[16] <= `ZERO_WORD;
			rf[17] <= `ZERO_WORD;
			rf[18] <= `ZERO_WORD;
			rf[19] <= `ZERO_WORD;
			rf[20] <= `ZERO_WORD;
			rf[21] <= `ZERO_WORD;
			rf[22] <= `ZERO_WORD;
			rf[23] <= `ZERO_WORD;
			rf[24] <= `ZERO_WORD;
			rf[25] <= `ZERO_WORD;
			rf[26] <= `ZERO_WORD;
			rf[27] <= `ZERO_WORD;
			rf[28] <= `ZERO_WORD;
			rf[29] <= `ZERO_WORD;
			rf[30] <= `ZERO_WORD;
			rf[31] <= `ZERO_WORD;
		end
		else 
		begin
			if ((wen == 0'b1) && (waddr != 5'h00))	
				rf[waddr] <= wdata;
		end
	end
	
	always @(*) begin
		if (rst == 0'b1)
			rdata1 = `ZERO_WORD;
		else if (ren1 == 0'b1)
			rdata1 = rf[raddr1];
		else
			rdata1 = `ZERO_WORD;
	end
	
	always @(*) begin
		if (rst == 0'b1)
			rdata2 = `ZERO_WORD;
		else if (ren2 == 0'b1)
			rdata2 = rf[raddr2];
		else
			rdata2 = `ZERO_WORD;
	end

  assign DPIC_res = rf[0];

endmodule