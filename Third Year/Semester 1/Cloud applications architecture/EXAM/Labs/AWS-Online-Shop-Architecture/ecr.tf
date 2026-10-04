resource "aws_ecr_repository" "repository" {
  name = "repository"

  image_scanning_configuration {
    scan_on_push = true
  }
}