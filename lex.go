package main

import (
	"fmt"
	"math/rand"
	"strings"
	"time"
)

type AssocArray interface {
	Assign(s string, x int)
	Lookup(s string) (x int, exists bool)
}

type SkipNode struct {
	key   string
	value int
	next  []*SkipNode
}

type SkipList struct {
	head      *SkipNode
	maxLevel  int
	currLevel int
}

func makeSkipList() AssocArray {
	rand.Seed(time.Now().UnixNano())
	return &SkipList{
		head:      &SkipNode{next: make([]*SkipNode, 16)},
		maxLevel:  16,
		currLevel: 1,
	}
}

func (sl *SkipList) Assign(key string, value int) {
	prev := make([]*SkipNode, sl.maxLevel)
	current := sl.head

	for i := sl.currLevel - 1; i >= 0; i-- {
		for current.next[i] != nil && current.next[i].key < key {
			current = current.next[i]
		}
		prev[i] = current
	}

	current = current.next[0]

	if current != nil && current.key == key {
		current.value = value
		return
	}

	newLevel := sl.randomLevel()

	if newLevel > sl.currLevel {
		for i := sl.currLevel; i < newLevel; i++ {
			prev[i] = sl.head
		}
		sl.currLevel = newLevel
	}

	newNode := &SkipNode{
		key:   key,
		value: value,
		next:  make([]*SkipNode, newLevel),
	}

	for i := 0; i < newLevel; i++ {
		newNode.next[i] = prev[i].next[i]
		prev[i].next[i] = newNode
	}
}

func (sl *SkipList) Lookup(key string) (int, bool) {
	current := sl.head

	for i := sl.currLevel - 1; i >= 0; i-- {
		for current.next[i] != nil && current.next[i].key < key {
			current = current.next[i]
		}
	}

	current = current.next[0]

	if current != nil && current.key == key {
		return current.value, true
	}

	return 0, false
}

func (sl *SkipList) randomLevel() int {
	level := 1
	for level < sl.maxLevel && rand.Float64() < 0.5 {
		level++
	}
	return level
}

type AVLNode struct {
	key    string
	value  int
	height int
	left   *AVLNode
	right  *AVLNode
}

type AVLTree struct {
	root *AVLNode
}

func makeAVL() AssocArray {
	return &AVLTree{}
}

func (avl *AVLTree) Assign(key string, value int) {
	avl.root = avl.insertNode(avl.root, key, value)
}

func (avl *AVLTree) Lookup(key string) (int, bool) {
	node := avl.findNode(avl.root, key)
	if node == nil {
		return 0, false
	}
	return node.value, true
}

func (avl *AVLTree) insertNode(node *AVLNode, key string, value int) *AVLNode {
	if node == nil {
		return &AVLNode{
			key:    key,
			value:  value,
			height: 1,
		}
	}

	if key < node.key {
		node.left = avl.insertNode(node.left, key, value)
	} else if key > node.key {
		node.right = avl.insertNode(node.right, key, value)
	} else {
		node.value = value
		return node
	}

	node.height = 1 + avl.max(avl.getHeight(node.left), avl.getHeight(node.right))

	balance := avl.getBalance(node)

	if balance > 1 && key < node.left.key {
		return avl.rotateRight(node)
	}

	if balance < -1 && key > node.right.key {
		return avl.rotateLeft(node)
	}

	if balance > 1 && key > node.left.key {
		node.left = avl.rotateLeft(node.left)
		return avl.rotateRight(node)
	}

	if balance < -1 && key < node.right.key {
		node.right = avl.rotateRight(node.right)
		return avl.rotateLeft(node)
	}

	return node
}

func (avl *AVLTree) findNode(node *AVLNode, key string) *AVLNode {
	if node == nil {
		return nil
	}

	if key < node.key {
		return avl.findNode(node.left, key)
	} else if key > node.key {
		return avl.findNode(node.right, key)
	} else {
		return node
	}
}

func (avl *AVLTree) getHeight(node *AVLNode) int {
	if node == nil {
		return 0
	}
	return node.height
}

func (avl *AVLTree) getBalance(node *AVLNode) int {
	if node == nil {
		return 0
	}
	return avl.getHeight(node.left) - avl.getHeight(node.right)
}

func (avl *AVLTree) max(a, b int) int {
	if a > b {
		return a
	}
	return b
}

func (avl *AVLTree) rotateLeft(x *AVLNode) *AVLNode {
	y := x.right
	T2 := y.left

	y.left = x
	x.right = T2

	x.height = avl.max(avl.getHeight(x.left), avl.getHeight(x.right)) + 1
	y.height = avl.max(avl.getHeight(y.left), avl.getHeight(y.right)) + 1

	return y
}

func (avl *AVLTree) rotateRight(y *AVLNode) *AVLNode {
	x := y.left
	T2 := x.right

	x.right = y
	y.left = T2

	y.height = avl.max(avl.getHeight(y.left), avl.getHeight(y.right)) + 1
	x.height = avl.max(avl.getHeight(x.left), avl.getHeight(x.right)) + 1

	return x
}

func lex(sentence string, array AssocArray) []int {
	words := strings.Fields(sentence)
	result := make([]int, 0, len(words))
	nextID := 1

	for _, word := range words {
		id, exists := array.Lookup(word)

		if exists {
			result = append(result, id)
		} else {
			array.Assign(word, nextID)
			result = append(result, nextID)
			nextID++
		}
	}

	return result
}

func main() {
	testSentence := "alpha x1 beta alpha x1 y"

	fmt.Println("Лексический анализ строки:", testSentence)
	fmt.Println()

	fmt.Println("1. Тест со списком с пропусками:")
	skipList := makeSkipList()
	result1 := lex(testSentence, skipList)
	fmt.Println("Результат:", result1)
	fmt.Println()

	fmt.Println("2. Тест с АВЛ-деревом:")
	avlTree := makeAVL()
	result2 := lex(testSentence, avlTree)
	fmt.Println("Результат:", result2)
	fmt.Println()

	fmt.Println("3. Сравнение результатов:")
	if compareSlices(result1, result2) {
		fmt.Println("Результаты одинаковые - тест пройден!")
	} else {
		fmt.Println("Результаты разные - есть ошибка!")
	}
}

func compareSlices(a, b []int) bool {
	if len(a) != len(b) {
		return false
	}
	for i := range a {
		if a[i] != b[i] {
			return false
		}
	}
	return true
}