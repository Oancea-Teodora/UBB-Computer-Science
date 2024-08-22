#include "ShoppingBasketWidget.h"

ShoppingBasketWidget::ShoppingBasketWidget(ShoppingBasket& basket, QWidget *parent)
	: QWidget(parent), basket{basket}
{
	ui.setupUi(this);
	this->populateList();

	this->ui.priceLabel->setText("Total price: " + QString::number(basket.getTotalPrice()));

	QObject::connect(this->ui.addButton, &QPushButton::clicked, [this]() {
		std::vector<Product> products{ Product{"Milk", 5}, Product{"Honey", 30}, Product{"Toy car", 50} };
		int index = rand() % 3;
		Product p = products[index];
		this->basket.addProduct(p);
		this->populateList();
		});
}

ShoppingBasketWidget::~ShoppingBasketWidget()
{
}

void ShoppingBasketWidget::populateList()
{
	this->ui.productsListWidget->clear();

	for (Product p : this->basket.getAll())
		this->ui.productsListWidget->addItem(QString::fromStdString(p.toString()));
}

void ShoppingBasketWidget::update()
{
	this->populateList();
	this->ui.priceLabel->setText("Total price: " + QString::number(basket.getTotalPrice()));
}
