#include "ListIterator.h"
#include "IndexedList.h"
#include <exception>
#include <iostream>

using namespace std;

ListIterator::ListIterator(IndexedList& list) : list(list){
   //TODO - Implementation
    this->current = this->list.head;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

void ListIterator::first(){
    //TODO - Implementation
    this->current = this->list.head;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

void ListIterator::next(){
    //TODO - Implementation
    if (this->current == -1)
		throw exception();
    this->current = this->list.array[this->current].next;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

bool ListIterator::valid() const{
    //TODO - Implementation
	return this->current != -1;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

TElem ListIterator::getCurrent() const{
   //TODO - Implementation
    if (this->current != -1)
        return this->list.array[this->current].value;
    else
        throw exception();
	return NULL_TELEM;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

TElem ListIterator::remove()
{
    if(this->current == -1)
		throw exception();
	int poz = this->current;
    auto old = this->list.array[poz].value;
    this->current = this->list.array[this->current].next;
    if (poz == 0)
    {
        auto elem = this->list.array[this->list.head].next;
        this->list.array[this->list.head].value = NULL_TELEM;
        this->list.head = elem;
        this->list.sizee--;
	}
    else
    {
		int current1 = this->list.head;
        int previous = -1;
        while (poz != 1)
        {
            previous = current1;
			current1 = this->list.array[current1].next;
            poz--;
            if(this->list.array[current1].next == -1 && this->list.array[current1].value == old)
            {
                this->list.array[current1].value = NULL_TELEM;
                this->list.array[previous].next = this->list.array[current1].next;
                this->list.array[current1].next = this->list.firstEmpty;
                this->list.firstEmpty = current1;
                this->list.sizee--;
                this->current = -1;
                return old;
            }
		}
        this->list.array[current1].value = NULL_TELEM;
		this->list.array[previous].next = this->list.array[current1].next;
		this->list.array[current1].next = this->list.firstEmpty;
		this->list.firstEmpty = current1;
		this->list.sizee--;
    }
    return old;
	
}
// BC: Theta(1)
// WC: Theta(capacity)
// TC: O(capacity)
