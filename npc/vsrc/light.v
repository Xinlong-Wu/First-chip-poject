module light(
  input clk,
  input rst,
  output reg [11:0] led
);
  reg [31:0] count;
  always @(posedge clk) begin
    if (rst) begin led <= 1; count <= 0; end
    else begin
      if (count == 0) led <= {led[10:0], led[11]};
      count <= (count >= 50000 ? 32'b0 : count + 1);
    end
  end
endmodule