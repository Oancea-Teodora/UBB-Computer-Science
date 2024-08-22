#include <exception>

#include "IndexedList.h"
#include "ListIterator.h"
#include <iostream>

using namespace std;

void IndexedList::resize()
{
	this->capacity *= 2;
	Node* newArray = new Node[this->capacity];
	for (int i = 0;i < this->capacity / 2;i++)
	{
		newArray[i].value = this->array[i].value;
		newArray[i].next = this->array[i].next;
	}
	for (int i = this->capacity / 2;i < this->capacity - 1;i++)
	{
		newArray[i].next = i+1;
		newArray[i].value = NULL_TELEM;
	}

	delete[] this->array;
	this->array = newArray;
	this->firstEmpty = this->capacity / 2;
}
// BC: Theta(capacity)
// WC: Theta(capacity)
// TC: Theta(capacity)

IndexedList::IndexedList() {
	//TODO - Implementation
    this->capacity = 2;
    this->head = -1;
    this->sizee = 0;
    this->firstEmpty = 0;
    this->array = new Node[capacity];
    for (int i = 0;i < capacity - 1;i++)
    {
        this->array[i].next = i+1;
        this->array[i].value = NULL_TELEM;
    }
	this->array[capacity - 1].next = -1;
}
// BC: Theta(capacity)
// WC: Theta(capacity)
// TC: Theta(capacity)

int IndexedList::size() const {
    //TODO - Implementation
	return this->sizee;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)


bool IndexedList::isEmpty() const {
    //TODO - Implementation
	return this->sizee == 0;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

TElem IndexedList::getElement(int pos) const {
    //TODO - Implementation
    if (pos < 0 || pos >= this->sizee)
        throw exception();
    int current = this->head;
    while (current != -1)
    {
        if (pos == 0)
			return this->array[current].value;
		pos--;
		current = this->array[current].next;
    }
	if(current == -1)
		throw exception();

	return NULL_TELEM;
}
// BC: Theta(1) WC: Theta(size) TC: O(size)

TElem IndexedList::setElement(int pos, TElem e) {
    //TODO - Implementation
    if (pos < 0 || pos >= this->sizee)
		throw exception();
	int current = this->head;
    while (current != -1)
    {
        if (pos == 0)
        {
			TElem old = this->array[current].value;
			this->array[current].value = e;
			return old;
		}
		pos--;
		current = this->array[current].next;
	}
	if(current == -1)
		throw exception();	
	return NULL_TELEM;
}
// BC: Theta(1) WC: Theta(size) TC: O(size)

void IndexedList::addToEnd(TElem e) {
    //TODO - Implementation
	if (this->sizee == this->capacity)
	{
		this->resize();
	}
	
    if (this->sizee == 0)
    {
		auto elem= this->array[firstEmpty].next;
		this->array[this->firstEmpty].value = e;
		this->array[this->firstEmpty].next = -1;
		this->head = this->firstEmpty;
		this->firstEmpty = elem;
		this->sizee++;
	
	}
    else
    {
		int current = this->head;
        while (this->array[current].next != -1)
        {
			current = this->array[current].next;
		}
		auto elem= this->array[firstEmpty].next;
		this->array[current].next = this->firstEmpty;
		this->array[this->firstEmpty].value = e;
		this->array[this->firstEmpty].next = -1;
		this->firstEmpty = elem;
		this->sizee++;
	}
}
// BC: Theta(1) WC: Theta(size) TC: O(size)

void IndexedList::addToPosition(int pos, TElem e) {
    //TODO - Implementation
	if (pos < 0 || pos >= this->sizee)
		throw exception();
	if(this->sizee == this->capacity)
		this->resize();
	if (pos == 0)
	{
		auto elem = this->array[firstEmpty].next;
		this->array[this->firstEmpty].value = e;
		this->array[this->firstEmpty].next = this->head;
		this->head = this->firstEmpty;
		this->firstEmpty = elem;
		this->sizee++;	
	}
	else
	{
		int current = this->head;
		int sem = 0;
		while (sem == 0)
		{
			if (pos == 1)
			{
				auto elem = this->array[firstEmpty].next;
				this->array[this->firstEmpty].value = e;
				this->array[this->firstEmpty].next = this->array[current].next;
				this->array[current].next = this->firstEmpty;
				this->firstEmpty = elem;
				this->sizee++;
				sem = 1;
			}
			pos--;
			current = this->array[current].next;
		}
	
	}
}
// BC: Theta(1) WC: Theta(size) TC: O(size)

TElem IndexedList::remove(int pos) {
    //TODO - Implementation
	if (pos < 0 || pos >= this->sizee)
		throw exception();
	if (pos == 0)
	{
		auto elem = this->array[this->head].next;
		TElem old = this->array[this->head].value;
		this->array[this->head].value = NULL_TELEM;
		this->head = elem;
		this->sizee--;
		return old;
	}
	else
	{
		int current = this->head;
		while (pos != 1)
		{
			current = this->array[current].next;
			if (current == -1 || this->array[current].next == -1)
				throw exception();
			pos--;
		}
		
		int toRemove = this->array[current].next;
		auto old = this->array[toRemove].value;
		this->array[current].next = this->array[toRemove].next;
		this->array[toRemove].value = NULL_TELEM;
		this->array[toRemove].next = this->firstEmpty;
		this->firstEmpty = toRemove;

		this->sizee--;
		return old;
	}
	return NULL_TELEM;
}
// BC: Theta(1) WC: Theta(size) TC: O(size)

int IndexedList::search(TElem e) const{
    //TODO - Implementation
	int current = this->head;
	int pos = 0;
	while (current != -1)
	{
		if (this->array[current].value == e)
			return pos;
		pos++;
		current = this->array[current].next;
	}
	return -1;
}
// BC: Theta(1) WC: Theta(size) TC: O(size)

ListIterator IndexedList::iterator(){
    return ListIterator(*this);        
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

IndexedList::~IndexedList() {
	//TODO - Implementation
	delete[] this->array;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)