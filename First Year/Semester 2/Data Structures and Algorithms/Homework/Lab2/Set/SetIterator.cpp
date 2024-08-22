#include "SetIterator.h"
#include "Set.h"
#include <exception>


SetIterator::SetIterator(const Set& m) : set(m)
{
	//TODO - Implementation
	this->current = this->set.head;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)


void SetIterator::first() {
	//TODO - Implementation
	this->current = this->set.head;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

void SetIterator::next() {
	//TODO - Implementation
	if (this->current == nullptr)
		throw std::exception("Invalid iterator");
	this->current = this->current->next;

}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

TElem SetIterator::getCurrent()
{
	//TODO - Implementation
	if(this->current == nullptr)
		throw std::exception("Invalid iterator");
	if (this->current != nullptr)
		return this->current->values;
	return NULL_TELEM;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

bool SetIterator::valid() const {
	//TODO - Implementation
	if (this->current != nullptr)
		return true;
	return false;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

void SetIterator::jumpBackward(int k)
{
	if (this->current == nullptr || k<=0)
		throw std::exception("Invalid");
	for (int i = 0; i < k; i++)
	{
		if (this->current == nullptr)
			throw std::exception("Invalid iterator");
		this->current = this->current->prev;
	}
}
// BC: Theta(1) - when the number of steps is negative (or 0) or the iterator is invalid
// WC: Theta(k) 
// TC: O(k)
