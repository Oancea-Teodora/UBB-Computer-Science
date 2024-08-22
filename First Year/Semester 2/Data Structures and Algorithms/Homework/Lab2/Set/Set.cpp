#include "Set.h"
#include "SetITerator.h"

Set::Set() {
	//TODO - Implementation
	this->head = nullptr;
	this->tail = nullptr;
	this->length = 0;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

bool Set::add(TElem elem) {
	//TODO - Implementation
	if (search(elem) == true)
		return false;
	else
	{
		Node* newN = new Node;
		newN->values = elem;
		newN->prev = this->tail;
		newN->next = nullptr;
		if (this->head == nullptr)
		{
			this->head = newN;
			this->tail = newN;
		}
		else
		{
			this->tail->next = newN;
			this->tail = newN;
		}
		this->length++;
		return true;

	}
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

bool Set::remove(TElem elem) {
	//TODO - Implementation
	if (search(elem) == false)
		return false;
	else
	{
		Node* current = this->head;
		Node* prev = nullptr;
		while (current != nullptr)
		{
			if (current->values == elem)
			{
				if (current == this->head)
				{
					this->head = current->next;
					if (this->head != nullptr)
						this->head->prev = nullptr;
				}
				else if (current == this->tail)
				{
					this->tail = current->prev;
					this->tail->next = nullptr;
				}
				else
				{
					current->prev->next = current->next;
					current->next->prev = current->prev;
				}
				delete current;
				this->length--;
				return true;
			}
			current = current->next;
		}
	}
	return false;
}
// BC: Theta(1) - when the element is on the first position in the set
// WC: Theta(set_size) - when the element is on the last position in the set
// TC: O(set_size)

bool Set::search(TElem elem) const {
	//TODO - Implementation
	Node* current = this->head;
	if (this->head == nullptr)
		return false;
	if (this->head->values == elem)
		return true;
	while (current != nullptr)
	{
		if (current->values == elem)
			return true;
		current = current->next;
	}
	return false;
}
// BC: Theta(1) - when the element is on the first position in the set or the set is empty
// WC: Theta(set_size) - when the element is on the last position in the set or it is not in the set
// TC: O(set_size)

int Set::size() const {
	//TODO - Implementation
	return this->length;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)


bool Set::isEmpty() const {
	//TODO - Implementation
	if (this->length == 0)
		return true;
	return false;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)


Set::~Set() {
	//TODO - Implementation
	Node* current = this->head;
	while (current != nullptr)
	{
		Node* next = current->next;
		delete current;
		current = next;
	}
	this->head = nullptr;
	this->tail = nullptr;
	this->length = 0;
}
// BC: Theta(set_size) WC: Theta(set_size) TC: Theta(set_size)


SetIterator Set::iterator() const{
	return SetIterator(*this);
}


