import (
	"container/heap"
)

// Item представляет элемент в очереди с приоритетами
type Item struct {
	value    float64 // значение
	source   int     // индекс источника в срезе sources
}

// PriorityQueue реализует очередь с приоритетами
type PriorityQueue []*Item

// Реализация методов интерфейса heap.Interface

func (pq PriorityQueue) Len() int { return len(pq) }

func (pq PriorityQueue) Less(i, j int) bool {
	return pq[i].value < pq[j].value
}

func (pq PriorityQueue) Swap(i, j int) {
	pq[i], pq[j] = pq[j], pq[i]
}

func (pq *PriorityQueue) Push(x interface{}) {
	item := x.(*Item)
	*pq = append(*pq, item)
}

func (pq *PriorityQueue) Pop() interface{} {
	old := *pq
	n := len(old)
	item := old[n-1]
	old[n-1] = nil // избегаем утечки памяти
	*pq = old[0 : n-1]
	return item
}

// MergeSeqs реализует слияние отсортированных последовательностей
func MergeSeqs(sources []chan float64, target chan float64) {
	defer close(target)

	n := len(sources)
	if n == 0 {
		return
	}

	// Создаем и инициализируем кучу
	pq := make(PriorityQueue, 0)
	heap.Init(&pq)

	// Читаем первый элемент из каждого канала и добавляем в кучу
	for i, ch := range sources {
		if value, ok := <-ch; ok {
			heap.Push(&pq, &Item{
				value:  value,
				source: i,
			})
		}
	}

	// Основной цикл слияния
	for pq.Len() > 0 {
		// Извлекаем минимальный элемент
		item := heap.Pop(&pq).(*Item)
		target <- item.value

		// Читаем следующий элемент из того же источника
		if nextValue, ok := <-sources[item.source]; ok {
			heap.Push(&pq, &Item{
				value:  nextValue,
				source: item.source,
			})
		}
	}
}
