#pragma once
#include <string>

class Product
{
private:
	std::string name;
	double price;
	double quantity;

public:
	Product(const std::string& name, double price, double quantity);
	std::string getName() const;
	double getPrice() const;
	double getQuantity() const;
	double setQuantity(double val);

	std::string toString() const;
};

