#include "SortedBag.h"
#include "SortedBagIterator.h"

void SortedBag::resize()
{
	int old = this->capacity;
	this->capacity = 2 * this->capacity;
	Array* newArray = new Array[capacity];
	for (int i = 0;i < old; i++)
		newArray[i] = arr[i];

	for (int i = old;i < this->capacity;i++)
	{
		newArray[i].info = NULL_TCOMP;
		newArray[i].left = i + 1;
		newArray[i].right = -1;
	}

	delete[] arr;
	arr = new Array[capacity];
	for (int i = 0;i < capacity; i++)
		arr[i] = newArray[i];
		
}

SortedBag::SortedBag(Relation r) {
	//TODO - Implementation
	this->capacity = 10;
	this->arr = new Array[capacity];
	this->nrElements = 0;
	this->root = -1;
	this->firstFree = 0;
	this->r = r;
	for (int i = 0; i < capacity; i++)
	{
		arr[i].info = NULL_TCOMP;
		arr[i].left = i+1;
		arr[i].right = -1;
	}	
}
//BC: Theta(n) WC: Theta(n) TC: Theta(n)

void SortedBag::add(TComp e) {
	//TODO - Implementation
	this->nrElements++;
	if (this->nrElements > this->capacity)
		resize();

	int oldFirstFreeLeft = arr[firstFree].left;

	arr[firstFree].info = e;
	arr[firstFree].left = -1;
	arr[firstFree].right = -1;

	int current = this->root;
	int parent = -1;
	while (current != -1)
	{
		parent = current;
		if (r(e, arr[current].info))
			current = arr[current].left;
		else
			current = arr[current].right;
	}

	if (this->root == -1)
		this->root = firstFree;
	else if (r(e, arr[parent].info))
		arr[parent].left = firstFree;
	else
		arr[parent].right = firstFree;

	this->firstFree = oldFirstFreeLeft;
	
}
//BC: Theta(1) WC: Theta(n) TC: O(n)


bool SortedBag::remove(TComp e) {
	//TODO - Implementation
	if(this->root == -1 || !search(e))
		return false;
	
	int current = this->root;
	int parent = -1;
	bool isLeftChild = true;

	while (current != -1 && arr[current].info != e)
	{
		parent = current;
		if (r(e, arr[current].info))
		{
			isLeftChild = true;
			current = arr[current].left;
		}
		else
		{
			isLeftChild = false;
			current = arr[current].right;
		}
	}

	if (arr[current].left == -1 && arr[current].right == -1)
	{
		if (current == this->root)
			this->root = -1;
		else if (isLeftChild)
			arr[parent].left = -1;
		else
			arr[parent].right = -1;
	}
	else if (arr[current].right == -1)
	{
		if (current == this->root)
			this->root = arr[current].left;
		else if (isLeftChild)
			arr[parent].left = arr[current].left;
		else
			arr[parent].right = arr[current].left;
	}
	else if (arr[current].left == -1)
	{
		if (current == this->root)
			this->root = arr[current].right;
		else if (isLeftChild)
			arr[parent].left = arr[current].right;
		else
			arr[parent].right = arr[current].right;
	}
	else
	{
		// find the rightmost node in the left subtree
		int replacerParent = current;
		int replacer = arr[current].left;
		while (arr[replacer].right != -1)
		{
			replacerParent = replacer;
			replacer = arr[replacer].right;
		}
		arr[current].info = arr[replacer].info;
		if (replacerParent != current)
			arr[replacerParent].right = arr[replacer].left;
		else
			arr[replacerParent].left = arr[replacer].left;
		current = replacer;
	}

	arr[current].left = firstFree;
	firstFree = current;
	arr[current].info = NULL_TCOMP;
	this->nrElements--;
	return true;

}
//BC: Theta(1) WC: Theta(n) TC: O(n)


bool SortedBag::search(TComp elem) const {
	//TODO - Implementation
	int current = this->root;
	if(current == -1)
		return false;
	while (current != -1)
	{
		if (arr[current].info == elem)
		{
			return true;
			break;
		}
		else if (r(elem, arr[current].info))
			current = arr[current].left;
		else if (!r(elem, arr[current].info))
			current = arr[current].right;
	}
	return false;
}
//BC: Theta(1) WC: Theta(n) TC: O(n)


int SortedBag::nrOccurrences(TComp elem) const {
	//TODO - Implementation
	int current = this->root;
	int nr = 0;
	if (current == -1)
		return 0;
	while (current != -1)
	{
		if (arr[current].info == elem)
		{
			nr++;
		}
		if (r(elem, arr[current].info))
			current = arr[current].left;
		else if (!r(elem, arr[current].info))
			current = arr[current].right;
	}
	return nr;
}
//BC: Theta(1) WC: Theta(n) TC: O(n)



int SortedBag::size() const {
	//TODO - Implementation
	return this->nrElements;
}
//BC: Theta(1) WC: Theta(1) TC: Theta(1)


bool SortedBag::isEmpty() const {
	//TODO - Implementation
	return this->nrElements == 0;
}
//BC: Theta(1) WC: Theta(1) TC: Theta(1)

int SortedBag::getRange() const
{
	if(this->isEmpty())
		return -1;
	SortedBagIterator it = this->iterator();
	it.first();
	int min = it.getCurrent();
	int max = it.getCurrent();
	while (it.valid())
	{
		max = it.getCurrent();
		it.next();
	}
	return max - min;
}
//BC: Theta(n) WC: Theta(n) TC: Theta(n)


SortedBagIterator SortedBag::iterator() const {
	return SortedBagIterator(*this);
}


SortedBag::~SortedBag() {
	//TODO - Implementation
	delete[] arr;
}
