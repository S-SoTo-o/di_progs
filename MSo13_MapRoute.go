package main

func MergeSort(items int, compare func(i, j int) int, indices chan int) {
    if items <= 0 {
        close(indices)
        return
    }
    go parallelMergeSort(compare, 0, items-1, indices)
}

func parallelMergeSort(compare func(i, j int) int, start, end int, output chan int) {
    if start == end {
        output <- start
        close(output)
        return
    }
    
    mid := start + (end-start)/2
    leftChan := make(chan int)
    rightChan := make(chan int)
    
    go parallelMergeSort(compare, start, mid, leftChan)
    go parallelMergeSort(compare, mid+1, end, rightChan)
    
    mergeChannels(compare, leftChan, rightChan, output)
}

func mergeChannels(compare func(i, j int) int, left, right, result chan int) {
    leftVal, leftOk := <-left
    rightVal, rightOk := <-right
    
    for leftOk && rightOk {
        if compare(leftVal, rightVal) <= 0 {
            result <- leftVal
            leftVal, leftOk = <-left
        } else {
            result <- rightVal
            rightVal, rightOk = <-right
        }
    }
    
    for leftOk {
        result <- leftVal
        leftVal, leftOk = <-left
    }
    
    for rightOk {
        result <- rightVal
        rightVal, rightOk = <-right
    }
    
    close(result)
}

func main() {}
