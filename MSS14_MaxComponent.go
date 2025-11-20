func MergeSeqSort(nitems int, compare func(i, j int) int, indices chan int) {
    if nitems == 0 {
        close(indices)
        return
    }

    // Создаем канал каналов с буфером nitems (разрешено условием)
    streams := make(chan chan int, nitems)
    
    // Инициализируем последовательность каналов длины 1
    for i := 0; i < nitems; i++ {
        ch := make(chan int) // синхронный канал
        streams <- ch
        go func(idx int) {
            ch <- idx
            close(ch)
        }(i)
    }
    close(streams)

    // Итеративно сливаем подпоследовательности
    for {
        // Собираем все каналы из streams
        var chanList []chan int
        for ch := range streams {
            chanList = append(chanList, ch)
        }
        
        if len(chanList) == 1 {
            // Последний канал - отсортированная последовательность
            for idx := range chanList[0] {
                indices <- idx
            }
            close(indices)
            return
        }

        // Создаем новый канал каналов для следующей итерации
        nextStreams := make(chan chan int, (len(chanList)+1)/2)
        
        // Попарно сливаем соседние подпоследовательности
        for i := 0; i < len(chanList); i += 2 {
            if i+1 < len(chanList) {
                merged := make(chan int)
                go merge(compare, chanList[i], chanList[i+1], merged)
                nextStreams <- merged
            } else {
                // Нечетный случай - передаем последний канал без изменений
                nextStreams <- chanList[i]
            }
        }
        close(nextStreams)
        streams = nextStreams
    }
}

func merge(compare func(i, j int) int, a, b, out chan int) {
    aVal, aOk := <-a
    bVal, bOk := <-b

    for aOk && bOk {
        cmp := compare(aVal, bVal)
        if cmp <= 0 {
            // Сохраняем стабильность: при равенстве берем элемент из первого канала
            out <- aVal
            aVal, aOk = <-a
        } else {
            out <- bVal
            bVal, bOk = <-b
        }
    }

    // Досылаем оставшиеся элементы
    for aOk {
        out <- aVal
        aVal, aOk = <-a
    }

    for bOk {
        out <- bVal
        bVal, bOk = <-b
    }

    close(out)
}
