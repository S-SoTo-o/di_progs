package main

import "fmt"

func qssort(n int, less func(i, j int) bool, swap func(i, j int)) {
	if n <= 1 {
		return
	}
	stack := make([][2]int, 0)
	stack = append(stack, [2]int{0, n - 1})

	for len(stack) > 0 {
		top := len(stack) - 1
		low, high := stack[top][0], stack[top][1]
		stack = stack[:top]

		if low < high {
			pivot := partition(low, high, less, swap)
			stack = append(stack, [2]int{low, pivot - 1})
			stack = append(stack, [2]int{pivot + 1, high})
		}
	}
}

func partition(low, high int, less func(i, j int) bool, swap func(i, j int)) int {
	mid := (low + high) / 2
	pivot := mid
	swap(pivot, high)
	i := low
	for j := low; j < high; j++ {
		if less(j, high) {
			swap(i, j)
			i++
		}
	}
	swap(i, high)
	return i
}

func main() {
	arr := []int{5, 3, 8, 1, 2, 7, 4, 6}
	n := len(arr)

	less := func(i, j int) bool {
		return arr[i] < arr[j]
	}

	swap := func(i, j int) {
		arr[i], arr[j] = arr[j], arr[i]
	}

	qssort(n, less, swap)
	fmt.Println("Отсортированный массив:", arr)
}