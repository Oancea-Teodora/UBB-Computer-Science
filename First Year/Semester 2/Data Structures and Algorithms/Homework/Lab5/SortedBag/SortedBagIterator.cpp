#include "SortedBagIterator.h"
#include "SortedBag.h"
#include <exception>
#include <iostream>

using namespace std;

SortedBagIterator::SortedBagIterator(const SortedBag& b) : bag(b) {
	//TODO - Implementation
	this->Scapacity = 10;
	this->stack = new int[Scapacity];
	this->Ssize = 0;
	this->first();
}

void SortedBagIterator::Sresize()
{
	int newCap = 2 * Scapacity;
	int* newStack = new int[newCap];
	for (int i = 0; i < Ssize;i++)
	{
		newStack[i] = this->stack[i];
	}
	this->Scapacity = newCap;
	delete[] this->stack;
	this->stack = newStack;
}
//BC: Theta(n) WC: Theta(n) TC: Theta(n)

TComp SortedBagIterator::getCurrent() {
	//TODO - Implementation
	if (!valid())
		throw exception();
	return bag.arr[current].info;
}
//BC: Theta(1) WC: Theta(1) TC: Theta(1)

bool SortedBagIterator::valid() {
	//TODO - Implementation
	return this->current != -1;
}
//BC: Theta(1) WC: Theta(1) TC: Theta(1)

void SortedBagIterator::next() {
	//TODO - Implementation
	if (!valid())
		throw exception();

	int node = this->current;
	if (bag.arr[node].right != -1)
	{
		node = bag.arr[node].right;
		
		while (node != -1)
		{
			if (Ssize == Scapacity)
				Sresize();
			stack[this->Ssize++] = node;
			node = bag.arr[node].left;
		}
		
	}
	if (Ssize > 0)
		current = stack[--Ssize];
	else
		current = -1;
}
//BC: Theta(1) WC: Theta(n) TC: O(n)

void SortedBagIterator::first() {
	//TODO - Implementation
	this->Ssize = 0;
	this->current = bag.root;

	while (this->current != -1)
	{
		//cout << current <<" "<<Ssize << "\n";
		if (this->Ssize == this->Scapacity)
			Sresize();
		stack[Ssize++] = current;
		current = bag.arr[current].left;
	}

	if (this->Ssize > 0)
		this->current = stack[--Ssize];
	else
		this->current = -1;
}
//BC: Theta(1) WC: Theta(n) TC: O(n)

SortedBagIterator::~SortedBagIterator()
{
	delete[] stack;
}

