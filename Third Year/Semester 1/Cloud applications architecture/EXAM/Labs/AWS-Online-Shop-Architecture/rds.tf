resource "aws_db_subnet_group" "db_subnet_group" {
  name       = "db_subnet_group"
  subnet_ids = [aws_subnet.private_subnet_3.id]
}

resource "aws_db_instance" "db_rds" {
  allocated_storage      = 10
  engine                 = "postgres"
  engine_version         = "12.7"
  instance_class         = "db.t2.micro"
  db_name                = "postgres_db"
  identifier             = "postgres"
  username               = "postgres"
  password               = "superSecretPassword"
  skip_final_snapshot    = true
  db_subnet_group_name   = aws_db_subnet_group.db_subnet_group.name
  vpc_security_group_ids = [aws_security_group.db_security_group.id]
}