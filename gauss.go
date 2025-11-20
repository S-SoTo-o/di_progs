package main

import "fmt"

type Fraction struct {
	top    int
	bottom int
}

func findGCD(a, b int) int {
	if a < 0 {
		a = -a
	}
	if b < 0 {
		b = -b
	}

	for b != 0 {
		temp := b
		b = a % b
		a = temp
	}
	return a
}

func makeFraction(top, bottom int) Fraction {
	if bottom == 0 {
		bottom = 1
	}

	gcd := findGCD(top, bottom)
	if gcd < 0 {
		gcd = -gcd
	}

	top = top / gcd
	bottom = bottom / gcd

	if bottom < 0 {
		top = -top
		bottom = -bottom
	}

	return Fraction{top, bottom}
}

func addFractions(a, b Fraction) Fraction {
	newTop := a.top*b.bottom + b.top*a.bottom
	newBottom := a.bottom * b.bottom
	return makeFraction(newTop, newBottom)
}

func subtractFractions(a, b Fraction) Fraction {
	newTop := a.top*b.bottom - b.top*a.bottom
	newBottom := a.bottom * b.bottom
	return makeFraction(newTop, newBottom)
}

func multiplyFractions(a, b Fraction) Fraction {
	newTop := a.top * b.top
	newBottom := a.bottom * b.bottom
	return makeFraction(newTop, newBottom)
}

func divideFractions(a, b Fraction) Fraction {
	newTop := a.top * b.bottom
	newBottom := a.bottom * b.top
	return makeFraction(newTop, newBottom)
}

func isZero(f Fraction) bool {
	return f.top == 0
}

func solveSystem(matrix [][]Fraction) ([]Fraction, bool) {
	n := len(matrix)

	copyMatrix := make([][]Fraction, n)
	for i := 0; i < n; i++ {
		copyMatrix[i] = make([]Fraction, n+1)
		for j := 0; j <= n; j++ {
			copyMatrix[i][j] = matrix[i][j]
		}
	}

	for col := 0; col < n; col++ {
		maxRow := col
		for row := col + 1; row < n; row++ {
			currentVal := copyMatrix[row][col]
			maxVal := copyMatrix[maxRow][col]

			currentFloat := float64(currentVal.top) / float64(currentVal.bottom)
			maxFloat := float64(maxVal.top) / float64(maxVal.bottom)

			if currentFloat*currentFloat > maxFloat*maxFloat {
				maxRow = row
			}
		}

		if isZero(copyMatrix[maxRow][col]) {
			return nil, false
		}

		if maxRow != col {
			temp := copyMatrix[col]
			copyMatrix[col] = copyMatrix[maxRow]
			copyMatrix[maxRow] = temp
		}

		pivot := copyMatrix[col][col]
		for j := col; j <= n; j++ {
			copyMatrix[col][j] = divideFractions(copyMatrix[col][j], pivot)
		}

		for i := col + 1; i < n; i++ {
			factor := copyMatrix[i][col]
			for j := col; j <= n; j++ {
				term := multiplyFractions(factor, copyMatrix[col][j])
				copyMatrix[i][j] = subtractFractions(copyMatrix[i][j], term)
			}
		}
	}

	solution := make([]Fraction, n)
	for i := n - 1; i >= 0; i-- {
		solution[i] = copyMatrix[i][n]
		for j := i + 1; j < n; j++ {
			term := multiplyFractions(copyMatrix[i][j], solution[j])
			solution[i] = subtractFractions(solution[i], term)
		}
	}

	return solution, true
}

func main() {
	var n int
	fmt.Scan(&n)

	matrix := make([][]Fraction, n)
	for i := 0; i < n; i++ {
		matrix[i] = make([]Fraction, n+1)
		for j := 0; j <= n; j++ {
			var num int
			fmt.Scan(&num)
			matrix[i][j] = makeFraction(num, 1)
		}
	}

	answer, hasSolution := solveSystem(matrix)

	if !hasSolution {
		fmt.Println("No solution")
	} else {
		for i := 0; i < n; i++ {
			fmt.Printf("%d/%d\n", answer[i].top, answer[i].bottom)
		}
	}
}