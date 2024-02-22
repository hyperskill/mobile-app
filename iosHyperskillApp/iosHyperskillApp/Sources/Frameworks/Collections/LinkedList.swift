import Foundation

final class LinkedList<T> {
    class Node<NodeValueType> {
        var value: NodeValueType
        var next: Node?
        weak var previous: Node?

        init(value: NodeValueType) {
            self.value = value
        }
    }

    private(set) var head: Node<T>?
    private(set) var tail: Node<T>?

    var isEmpty: Bool { self.head == nil }

    func append(value: T) {
        let newNode = Node(value: value)

        if let tail = self.tail {
            newNode.previous = tail
            tail.next = newNode
        } else {
            self.head = newNode
        }

        self.tail = newNode
    }

    @discardableResult
    func remove(node: Node<T>) -> T {
        let previous = node.previous
        let next = node.next

        if let previous {
            previous.next = next
        } else {
            self.head = next
        }
        next?.previous = previous

        if next == nil {
            self.tail = previous
        }

        node.previous = nil
        node.next = nil

        return node.value
    }

    @discardableResult
    func removeFirst() -> T? {
        guard let head = self.head else {
            return nil
        }

        return self.remove(node: head)
    }

    @discardableResult
    func removeLast() -> T? {
        guard let tail = self.tail else {
            return nil
        }

        return self.remove(node: tail)
    }

    func removeAll() {
        self.head = nil
        self.tail = nil
    }
}
