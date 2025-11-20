package main

import (
	"fmt"
)

type Node struct {
	op    byte
	left  *Node
	right *Node
	value string
}

func parse(s string) *Node {
	index := 0
	return parseHelper(s, &index)
}

func parseHelper(s string, index *int) *Node {
	if s[*index] == '(' {
		*index++
		op := s[*index]
		*index++
		left := parseHelper(s, index)
		right := parseHelper(s, index)
		*index++
		return &Node{op: op, left: left, right: right}
	} else {
		value := string(s[*index])
		*index++
		return &Node{value: value}
	}
}

func main() {
	var input string
	fmt.Scanln(&input)

	tree := parse(input)

	seen := make(map[string]bool)
	count := 0

	var traverse func(*Node) string
	traverse = func(node *Node) string {
		if node.op == 0 {
			return node.value
		}
		leftStr := traverse(node.left)
		rightStr := traverse(node.right)
		s := "(" + string(node.op) + leftStr + rightStr + ")"
		if !seen[s] {
			seen[s] = true
			count++
		}
		return s
	}

	traverse(tree)
	fmt.Println(count)
}