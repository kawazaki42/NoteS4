#include <cstddef>
#include <iostream>

/// Узел связного списка.
template<typename E>
struct Node {
    E value;
    Node *next;
};

/// Интерфейс (абстрактный класс) упорядоченной коллекции.
///
/// В частности его реализуют связный список и динамический массив
template<typename E>
class OrderedCollection {
    // все методы абстрактны
public:
    virtual void push_front(E elem) = 0;
    virtual void push_back(E elem) = 0;
    virtual void insert(E elem, size_t i) = 0;

    virtual E pop_front(E elem) = 0;  //< удалить первый
    virtual E pop_back(E elem) = 0;   //< удалить последний
    virtual E remove(size_t i) = 0;   //< удалить i-й

    /// Размер (кол-во фактически хранимых элементов)
    virtual size_t get_size() const = 0;

    /// Удалить все элементы массива
    virtual void clear() = 0;

    virtual E &get(size_t index) = 0;
};

///
template<typename E>
class LinkedList: public OrderedCollection<E> {
    Node<E> *head, *last;
    size_t size;

public:
    LinkedList();

    void push_front(E elem) override;
    void push_back(E elem) override;
    void insert(E elem, size_t i) override;

    E pop_front(E elem) override;  //< удалить первый
    E pop_back(E elem) override;   //< удалить последний
    E remove(size_t i) override;   //< удалить i-й

    /// Размер (кол-во фактически хранимых элементов)
    size_t get_size() const override;

    /// Удалить все элементы массива
    void clear() override;

    E &get(size_t index) override;

    Node<E> *get_node(size_t index);

// private:
    void print_list() const {
        for (auto cur{head}; cur != nullptr; cur = cur->next) {
            std::cout << cur->value << ' ';
        }
    }
};

template<typename E>
void LinkedList<E>::push_front( E element ) {
    // auto new_node = new Node{element, head};

    // *head = new_node;
    head = new Node<E>{element, head};

    if (last == nullptr) last = head;

    size++;
}

int main() {
    LinkedList<int> list;
    list.push_front(1);
    list.push_front(2);
    list.push_front(3);

    list.print_list();

    std::cout << std::endl;
}
