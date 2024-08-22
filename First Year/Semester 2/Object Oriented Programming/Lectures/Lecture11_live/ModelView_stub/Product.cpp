#include "Product.h"

Product::Product(const std::string& name, double price, double quantity): 
	name{name}, price{price}, quantity{quantity}
{
}

std::string Product::getName() const
{
	return this->name;
}

double Product::getPrice() const
{
	return this->price;
}

double Product::getQuantity() const
{
	return quantity;
}

double Product::setQuantity(double val)
{
	return this->quantity = val;
}

std::string Product::toString() const
{
	return this->name + " - " + std::to_string(this->price);
}
