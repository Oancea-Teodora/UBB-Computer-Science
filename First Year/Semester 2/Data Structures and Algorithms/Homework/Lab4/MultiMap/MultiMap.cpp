#include "MultiMap.h"
#include "MultiMapIterator.h"
#include <exception>
#include <iostream>

using namespace std;

MultiMap::MultiMap() {
	//TODO - Implementation
	this->m = 10; // capacity
	this->length = 0;
	this->loadFactor = 0.7;
	this->hashTable = new Node*[this->m];
	for(int i=0; i<this->m;i++)
		this->hashTable[i] = nullptr;
}
// BC: Theta(capacity) WC: Theta(capacity) TC: O(capacity)

void MultiMap::add(TKey c, TValue v) {
	//TODO - Implementation
	if ((double)(this->length / this->m) > this->loadFactor)
		this->resize();

	int pos = this->hashFunction(c);
	Node* newNode = new Node;
	newNode->info.first = c;
	newNode->info.second = v;
	newNode->next = this->hashTable[pos];
	this->hashTable[pos] = newNode;
	this->length++;
}
// BC: Theta(1) WC: Theta(capacity) TC: O(capacity)


bool MultiMap::remove(TKey c, TValue v) {
	//TODO - Implementation
	int pos = this->hashFunction(c);
	if(this->hashTable[pos] == nullptr)
		return false;
	Node* current = this->hashTable[pos];
	Node* previous = nullptr;

	if(current->info.first == c && current->info.second == v)
	{
		Node* next = current->next;
		this->hashTable[pos] = next;
		delete current;
		this->length--;
		return true;
	}

	while(current != nullptr && (current->info.first != c || current->info.second != v))
	{
		previous = current;
		current = current->next;
	}

	if(current == nullptr)
		return false;

	previous->next = current->next;
	delete current;
	this->length--;
	return true;
}
// BC: Theta(1) WC: Theta(capacity) TC: O(capacity)


vector<TValue> MultiMap::search(TKey c) const {
	//TODO - Implementation
	int pos = this->hashFunction(c);
	vector<TValue> values;
	if(this->hashTable[pos] == nullptr)
		return values;
	Node* current = this->hashTable[pos];
	while(current != nullptr)
	{
		if(current->info.first == c)
			values.push_back(current->info.second);
		current = current->next;
	}
	return values;
	
}
// BC: Theta(1) WC: Theta(capacity) TC: O(capacity)


int MultiMap::size() const {
	//TODO - Implementation
	return this->length;

}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)


bool MultiMap::isEmpty() const {
	//TODO - Implementation
	if(this->length == 0)
		return true;
	return false;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

MultiMapIterator MultiMap::iterator() {
	return MultiMapIterator(*this);
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)


MultiMap::~MultiMap() {
	//TODO - Implementation
	for(int i=0; i<this->m; i++)
	{
		Node* current;
		while(this->hashTable[i] != nullptr)
		{
			current = this->hashTable[i]->next;
			delete this->hashTable[i];
			this->hashTable[i] = current;
		}
	}
	delete[] this->hashTable;
}
// BC: Theta(capacity) WC: Theta(capacity) TC: O(capacity)

int MultiMap::hashFunction(TKey c) const
{
	if(c < 0)
		return (-c) % this->m;
	return c % this->m;
}
// BC: Theta(1) WC: Theta(1) TC: Theta(1)

void MultiMap::resize()
{
	this->m *= 2;
	Node** newHashTable = new Node*[this->m];
	for(int i=0; i<this->m; i++)
		newHashTable[i] = nullptr;
	for(int i=0; i<this->m/2; i++)
	{
		Node* current = this->hashTable[i];
		while(current != nullptr)
		{
			Node* next = current->next;
			int pos = this->hashFunction(current->info.first);
			current->next = newHashTable[pos];
			newHashTable[pos] = current;
			current = next;
		}
	}
	delete[] this->hashTable;
	this->hashTable = newHashTable;
}
// BC: Theta(capacity) WC: Theta(capacity+length) TC: O(capacity+length)
