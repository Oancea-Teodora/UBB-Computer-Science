#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_Observer_stub.h"

class Observer_stub : public QMainWindow
{
    Q_OBJECT

public:
    Observer_stub(QWidget *parent = nullptr);
    ~Observer_stub();

private:
    Ui::Observer_stubClass ui;
};
