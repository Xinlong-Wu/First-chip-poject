### Reference io is not fully initialized.
error：
```scala
when(io.En){
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
