resource "aws_ecs_cluster" "ecs_cluster" {
  name = "ecs_cluster"

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_ecs_task_definition" "ecs_task_definition" {
  container_definitions = jsonencode([
    {
      name      = "ecs_container"
      image     = "${aws_ecr_repository.repository.repository_url}"
      cpu       = 1024
      memory    = 900
      essential = true
      portMappings = [
        {
          protocol      = "tcp"
          containerPort = 8080
          hostPort      = 8080
        }
      ]
    }
  ])

  family                   = "ecs_task_definition_family"
  memory                   = 900
  cpu                      = 1024
  network_mode             = "host"
  requires_compatibilities = ["EC2"]

  execution_role_arn = aws_iam_role.ecs_execution_role.arn
  task_role_arn      = aws_iam_role.ecs_task_role.arn
}

resource "aws_ecs_service" "ecs_service" {
  name            = "ecs_service"
  cluster         = aws_ecs_cluster.ecs_cluster.id
  depends_on      = [aws_iam_role_policy_attachment.iam_role_policy_attchm, aws_alb_listener.alb_listener]
  task_definition = aws_ecs_task_definition.ecs_task_definition.arn
  desired_count   = 1

  load_balancer {
    target_group_arn = aws_alb_target_group.target_group.arn
    container_name   = "ecs_container"
    container_port   = 8080
  }
}