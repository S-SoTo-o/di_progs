package main

import (
	"fmt"
)

func findDivisors(n int) []int {
	divs := []int{1}
	for i := 2; i*i <= n; i++ {
		if n%i == 0 {
			divs = append(divs, i)
			if i*i != n {
				divs = append(divs, n/i)
			}
		}
	}
	if n > 1 {
		divs = append(divs, n)
	}
	return divs
}

func buildDivisorGraph(n int) map[int][]int {
	divs := findDivisors(n)
	graph := make(map[int][]int)

	for _, u := range divs {
		for _, v := range divs {
			if u%v == 0 && u != v {
				isDirect := true
				for _, w := range divs {
					if u%w == 0 && w%v == 0 && u != w && w != v {
						isDirect = false
						break
					}
				}
				if isDirect {
					graph[u] = append(graph[u], v)
				}
			}
		}
	}
	return graph
}

func printDOT(graph map[int][]int) {
	fmt.Println("graph {")
	for node := range graph {
		fmt.Println("\t", node)
	}
	for u, neighbors := range graph {
		for _, v := range neighbors {
			fmt.Printf("\t%d--%d\n", u, v)
		}
	}
	fmt.Println("}")
}

func main() {
	var x int
	fmt.Scan(&x)
	resultGraph := buildDivisorGraph(x)
	if len(resultGraph) == 0 && x == 1 {
		fmt.Println("graph {")
		fmt.Println("\t1")
		fmt.Println("}")
		return
	}
	printDOT(resultGraph)
}
