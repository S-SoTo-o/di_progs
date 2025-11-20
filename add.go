package main

import (
	"fmt"
)

func add(a, b []int32, p int) []int32 {
	var c []int32
	var carry int32
	for i := 0; i < len(a) || i < len(b) || carry > 0; i++ {
		var x, y int32
		if i < len(a) {
			x = a[i]
		}
		if i < len(b) {
			y = b[i]
		}
		sum := x + y + carry
		carry = sum / int32(p)
		digit := sum % int32(p)
		c = append(c, digit)
	}
	return c
}

func main() {
	a := []int32{1, 2, 3}
	b := []int32{4, 5, 6}
	p := 10
	c := add(a, b, p)
	fmt.Println(c)
}