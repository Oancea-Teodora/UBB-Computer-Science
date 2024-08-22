#include "MultiMapIterator.h"
#include "MultiMap.h"
#include <iostream>

using namespace std;

MultiMapIterator::MultiMapIterator(MultiMap& c): col(c) {
	//TODO - Implementation
	this->currentPos = 0;
	this->nrnext = 0;
	while(this->currentPos < this->col.m && this->col.hashTable[this->currentPos] == nullptr) {
		this->currentPos++;
	}
	if(this->currentPos < this->col.m) {
		this->prevNode = nullptr;
		this->currentNode = this->col.hashTable[this->currentPos];
	}
	else {
		this->prevNode = nullptr;
		this->currentNode = nullptr;
	}
}
// BC: Theta(1) WC: Theta(capacity) TC: O(capacity)

TElem MultiMapIterator::getCurrent() const{
	//TODO - Implementation
	if (this->valid() && this->col.size() > 0)
		return this->currentNode->info;
	else
		throw exception();

}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

bool MultiMapIterator::valid() const {
	//TODO - Implementation
	if(this->currentNode == nullptr || this->currentPos > this->col.m)
		return false;
	return true;
	
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

void MultiMapIterator::next() {
	//TODO - Implementation
	this->nrnext++;
	if(this->col.size() == 0)
		throw exception();
	if (this->valid()) {
		this->prevNode = this->currentNode;
		this->currentNode = this->currentNode->next;
		if (this->currentNode == nullptr) {
			this->currentPos++;
			while (this->currentPos < this->col.m && this->col.hashTable[this->currentPos] == nullptr) {
				this->currentPos++;
			}
			if (this->currentPos < this->col.m) {
				this->prevNode = nullptr;
				this->currentNode = this->col.hashTable[this->currentPos];
			}
			else {
				this->prevNode = nullptr;
				this->currentNode = nullptr;
			}
		}
	}
	else
		throw exception();
}
// BC: Theta(1) WC: Theta(capacity) TC: O(capacity)

void MultiMapIterator::first() {
	//TODO - Implementation
	this->currentPos = 0;
	this->nrnext = 0;
	while(this->currentPos < this->col.m && this->col.hashTable[this->currentPos] == nullptr) {
		this->currentPos++;
	}
	if(this->currentPos < this->col.m) {
		this->prevNode = nullptr;
		this->currentNode = this->col.hashTable[this->currentPos];
	}
	else {
		this->prevNode = nullptr;
		this->currentNode = nullptr;
	}
}

//move the iterator backwards with k positions
void MultiMapIterator::jumpBackward(int k)
{
	if (k <= 0 || !this->valid()) {
		throw exception(); 
	}

	if (k > this->nrnext) {
		throw exception(); 
	}

	this->nrnext = this->nrnext - k;
	int nr = this->nrnext;
	this->first();
	for (int i = 0; i < nr; i++) {
		if (!this->valid()) {
			throw exception(); 
		}
		this->next();
	}
}
