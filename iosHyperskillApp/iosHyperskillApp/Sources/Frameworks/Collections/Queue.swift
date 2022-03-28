import Foundation

struct Queue<T> {
    private var linkedList = LinkedList<T>()

    var isEmpty: Bool { self.linkedList.isEmpty }

    mutating func enqueue(value: T) {
        self.linkedList.append(value: value)
    }

    mutating func dequeue() -> T? {
        self.linkedList.removeFirst()
    }

    func peek() -> T? {
        self.linkedList.head?.value
    }
}
