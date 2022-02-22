### Reference io is not fully initialized.
error：Reference io is not fully initialized
```scala
when(io.En){
  io.y := 1.U
  ...
}.otherwise{
  io.y := 0.U
}
```
fix:
```scala
io.y := 0.U
when(io.En){
  ...
}
```
