
resource "aws_vpc" "main" {
  cidr_block       = "192.168.0.0/16"
  instance_tenancy = "default"
}

resource "aws_subnet" "public_subnet" {
  vpc_id                  = aws_vpc.main.id
  map_public_ip_on_launch = true
  availability_zone       = "eu-central-1a"
  cidr_block              = "192.168.0.0/24"
}

resource "aws_subnet" "private_subnet_1" {
  vpc_id            = aws_vpc.main.id
  availability_zone = "eu-central-1b"
  cidr_block        = "192.168.1.0/24"
}

resource "aws_subnet" "private_subnet_2" {
  vpc_id            = aws_vpc.main.id
  availability_zone = "eu-central-1c"
  cidr_block        = "192.168.2.0/24"
}

resource "aws_subnet" "private_subnet_3" {
  vpc_id            = aws_vpc.main.id
  availability_zone = "eu-central-1b"
  cidr_block        = "192.168.3.0/24"
}

resource "aws_internet_gateway" "internet_gateway" {
  vpc_id = aws_vpc.main.id
}

resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.internet_gateway.id
  }
}

resource "aws_route_table_association" "assuciation_public_subnet" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.public_route_table.id
}
