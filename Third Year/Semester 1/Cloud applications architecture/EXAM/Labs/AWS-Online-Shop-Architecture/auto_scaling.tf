data "aws_ami" "last_linux_2" {
  most_recent = true

  filter {
    name   = "name"
    values = ["amzn-ami*amazon-ecs-optimized"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  owners = ["amazon"]
}

data "template_file" "user_data" {
  template = <<EOF
   #!/bin/bash
   echo ECS_CLUSTER=${aws_ecs_cluster.ecs_cluster.name} >> /etc/ecs/ecs.config
  EOF
}

resource "aws_launch_configuration" "launch_configuration" {
  name_prefix          = "launch_configuration"
  image_id             = data.aws_ami.last_linux_2.id
  instance_type        = "t2.micro"
  iam_instance_profile = aws_iam_instance_profile.instance_profile.name
  security_groups      = [aws_security_group.instance_security_group.id]
  user_data            = data.template_file.user_data.rendered

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_autoscaling_group" "autoscaling_group" {
  name                      = "autoscaling_group"
  max_size                  = 2
  min_size                  = 1
  health_check_grace_period = 300
  health_check_type         = "EC2"
  desired_capacity          = 1
  force_delete              = true
  launch_configuration      = aws_launch_configuration.launch_configuration.id
  vpc_zone_identifier       = [aaws_subnet.private_subnet_1.id, ws_subnet.private_subnet_2.id]
  target_group_arns         = [aws_alb_target_group.target_group.arn]

  tag {
    key                 = "Name"
    value               = "IaC"
    propagate_at_launch = true
  }
}