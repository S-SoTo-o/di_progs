package main

import (
	"fmt"
)

func encode(utf32 []rune) []byte {
	result := []byte{}
	for _, r := range utf32 {
		if r <= 127 {
			result = append(result, byte(r))
		} else if r <= 2047 {
			result = append(result, 192+byte(r>>6), 128+byte(r&63))
		} else if r <= 65535 {
			result = append(result, 224+byte(r>>12), 128+byte((r>>6)&63), 128+byte(r&63))
		} else {
			result = append(result, 240+byte(r>>18),
			128+byte((r>>12)&63), 128+byte((r>>6)&63), 128+byte(r&63))
		}
	}
	return result
}

func decode(utf8 []byte) []rune {
	result := []rune{}
	for i := 0; i < len(utf8); {
		b := utf8[i]
		if b <= 127 {
			result = append(result, rune(b))
			i++
		} else if b <= 223 {
			r := rune(b&31)<<6 + rune(utf8[i+1]&63)
			result = append(result, r)
			i += 2
		} else if b <= 239 {
			r := rune(b&15)<<12 + rune(utf8[i+1]&63)<<6 + rune(utf8[i+2]&63)
			result = append(result, r)
			i += 3
		} else {
			r := rune(b&7)<<18+rune(utf8[i+1]&63)<<12+rune(utf8[i+2]&63)<<6+rune(utf8[i+3]&63)
			result = append(result, r)
			i += 4
		}
	}
	return result
}

func main() {
	input := []rune("Пример текста")
	encoded := encode(input)
	fmt.Println(encoded)
	decoded := decode(encoded)
	fmt.Println(string(decoded))
}