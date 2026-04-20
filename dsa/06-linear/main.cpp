#include <cstddef>

/// Узел связного списка.
template<typename E>
struct Node {
    E value;
    Node *next;
};

template<typename E>
class List {
    Node<E> *head, *last;
    size_t size;

public:
    List();

    void push_front(E elem);
    void push_back(E elem);
    void insert(E elem, size_t i);

    void pop_front(E elem);
    void pop_back(E elem);
    void remove(size_t i);

    size_t get_size() const;
    void clear();

    E &get(size_t index);
    Node<E> *get_node(size_t index);
};

int main() {

}
