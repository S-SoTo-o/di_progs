package main

import "fmt"

func PriorityQueue(in, out chan int) {
    var items []int
    
    defer close(out)
    
    for {
        if len(items) > 0 {
            select {
            case num, ok := <-in:
                if !ok {
                    // Канал in закрыт - отправляем все оставшиеся элементы
                    for len(items) > 0 {
                        maxIndex := findMaxIndex(items)
                        out <- items[maxIndex]
                        items = removeAtIndex(items, maxIndex)
                    }
                    return
                }
                // Добавляем новый элемент
                items = addItem(items, num)
                
            case out <- items[findMaxIndex(items)]:
                // Извлекаем и удаляем максимальный элемент
                maxIndex := findMaxIndex(items)
                items = removeAtIndex(items, maxIndex)
            }
        } else {
            // Очередь пуста - ждем только новые элементы
            num, ok := <-in
            if !ok {
                return
            }
            items = addItem(items, num)
        }
    }
}

// Вспомогательные функции с сложностью O(n)
func findMaxIndex(items []int) int {
    maxIndex := 0
    for i := 1; i < len(items); i++ {
        if items[i] > items[maxIndex] {
            maxIndex = i
        }
    }
    return maxIndex
}

func addItem(items []int, num int) []int {
    // Просто добавляем в конец - O(1)
    return append(items, num)
}

func removeAtIndex(items []int, index int) []int {
    items[index] = items[len(items)-1]
    return items[:len(items)-1]
}

func main() {
    in := make(chan int, 5)
    out := make(chan int, 5)
    
    go PriorityQueue(in, out)
    
    in <- 5
    in <- 10  
    in <- 3
    in <- 8
    in <- 1
    
    close(in)
    
    fmt.Println("Числа в порядке приоритета:")
    for i := 0; i < 5; i++ {
        num := <-out
        fmt.Println(num)
    }
}
