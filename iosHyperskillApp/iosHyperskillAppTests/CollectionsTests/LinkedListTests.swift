@testable
import iosHyperskillApp

import XCTest

class LinkedListTest: XCTestCase {
    private let numbers = [8, 2, 10, 9, 7, 5]

    private func makeLinkedList() -> LinkedList<Int> {
        let list = LinkedList<Int>()
        for number in numbers {
            list.append(value: number)
        }
        return list
    }

    func testEmptyList() {
        let list = LinkedList<Int>()
        XCTAssertTrue(list.isEmpty)
        XCTAssertNil(list.head)
        XCTAssertNil(list.tail)
    }

    // MARK: Append

    func testListWithOneElement() {
        let list = LinkedList<Int>()
        list.append(value: 123)

        XCTAssertFalse(list.isEmpty)

        XCTAssertNotNil(list.head)
        XCTAssertNil(list.head!.previous)
        XCTAssertNil(list.head!.next)
        XCTAssertEqual(list.head!.value, 123)

        XCTAssertNotNil(list.tail)
        XCTAssertNil(list.tail!.previous)
        XCTAssertNil(list.tail!.next)
        XCTAssertEqual(list.tail!.value, 123)

        XCTAssertTrue(list.head === list.tail)
    }

    func testListWithTwoElements() {
        let list = LinkedList<Int>()
        list.append(value: 123)
        list.append(value: 456)

        XCTAssertNotNil(list.head)
        XCTAssertEqual(list.head!.value, 123)

        XCTAssertNotNil(list.tail)
        XCTAssertEqual(list.tail!.value, 456)

        XCTAssertTrue(list.head !== list.tail)

        XCTAssertNil(list.head!.previous)
        XCTAssertTrue(list.head!.next === list.tail)
        XCTAssertTrue(list.tail!.previous === list.head)
        XCTAssertNil(list.tail!.next)
    }

    func testListWithThreeElements() {
        let list = LinkedList<Int>()
        list.append(value: 123)
        list.append(value: 456)
        list.append(value: 789)

        XCTAssertNotNil(list.head)
        XCTAssertEqual(list.head!.value, 123)

        let second = list.head!.next
        XCTAssertNotNil(second)
        XCTAssertEqual(second!.value, 456)

        XCTAssertNotNil(list.tail)
        XCTAssertEqual(list.tail!.value, 789)

        XCTAssertNil(list.head!.previous)
        XCTAssertTrue(list.head!.next === second)
        XCTAssertTrue(second!.previous === list.head)
        XCTAssertTrue(second!.next === list.tail)
        XCTAssertTrue(list.tail!.previous === second)
        XCTAssertNil(list.tail!.next)
    }

    // MARK: Remove

    func testRemoveLastOnListWithOneElement() {
        let list = LinkedList<Int>()
        list.append(value: 123)

        let value = list.removeLast()
        XCTAssertEqual(value, 123)

        XCTAssertTrue(list.isEmpty)
        XCTAssertNil(list.head)
        XCTAssertNil(list.tail)
    }

    func testRemoveFirst() {
        let list = makeLinkedList()
        let head = list.head
        let next = head!.next

        let value = list.removeFirst()
        XCTAssertEqual(value, 8)

        XCTAssertNil(head!.previous)
        XCTAssertNil(head!.next)

        XCTAssertNil(next!.previous)
        XCTAssertTrue(list.head === next)
    }

    func testRemoveFirstInEmptyList() {
        let list = LinkedList<Int>()
        XCTAssertNil(list.removeFirst())
    }

    func testRemoveLast() {
        let list = makeLinkedList()
        let tail = list.tail
        let previous = tail!.previous

        let value = list.removeLast()
        XCTAssertEqual(value, 5)

        XCTAssertNil(tail!.previous)
        XCTAssertNil(tail!.next)

        XCTAssertNil(previous!.next)
        XCTAssertTrue(list.tail === previous)
    }

    func testRemoveLastInEmptyList() {
        let list = LinkedList<Int>()
        XCTAssertNil(list.removeLast())
    }

    func testRemoveAll() {
        let list = makeLinkedList()
        list.removeAll()
        XCTAssertTrue(list.isEmpty)
        XCTAssertNil(list.head)
        XCTAssertNil(list.tail)
    }
}
