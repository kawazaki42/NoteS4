#include <cassert>
#include <cstddef>
#include <stdexcept>
#include <cstring>

template <typename E>
class DynamicArray {
    /// Указатель на выделенную память в куче
    E *arr = nullptr;

    /// Кол-во фактически хранимых элементов
    size_t size = 0;

    /// Кол-во выделенной памяти (в элементах)
    size_t capacity = 1;

public:

    DynamicArray() {
        arr = new E[capacity];
    }

    DynamicArray(size_t capacity) {
        if (capacity != 0) {
            this->capacity = capacity;
        }

        arr = new E[capacity];
    }

    ~DynamicArray() {
        delete[] arr;
        arr = nullptr;
    }

    DynamicArray<E> copy() const {
        DynamicArray<E> result(capacity);
        result.size = size;

        // !
        // result.arr = new E[capacity];

        memcpy(result.arr, this->arr, this->size * sizeof(E));

        return result;
    }

    /// Получить элемент по индексу `i`
    E get(size_t i) const {
        if (i < size)
            return arr[i];
        else
            throw std::invalid_argument("index out of bounds");
    }

    /// Получить элемент по индексу `i`
    void set(size_t i, E x) {
        if (i < size)
            arr[i] = x;
        else
            throw std::invalid_argument("index out of bounds");
    }

    /// Аналог `set`
    E &operator[](size_t i) {
        if (i < size) return arr[i];
        else throw std::invalid_argument("index out of bounds");
    }

    size_t get_size() const {
        return size;
    }

    size_t get_capacity() const {
        return capacity;
    }

    /// ...
    void add(E x) {
        if (size == capacity) {
            // TODO: realloc
        };
        arr[size++] = x;
    }
};


int main() {
    {
        DynamicArray<int> a;
        assert(a.get_size() == 0);
        assert(a.get_capacity() == 1);
    }

    {
        DynamicArray<int> a;
        a.add(67);
        assert(a.get(0) == 67);

        // NOTE: literally same
        assert(a.operator[](0) == 67);
        assert(a[0] == 67);

        assert(a.get_size() == 1);
        assert(a.get_capacity() == 1);
    }

    {
        DynamicArray<int> a;
        a.add(67);

        DynamicArray<int> b = a.copy();

        assert(a.get(0) == b.get(0));

        b.set(0, 42);

        assert(a.get(0) == 67);
        assert(b.get(0) == 42);

        assert(a.get_size() == b.get_size());
        assert(a.get_capacity() == b.get_capacity());
    }
}
