package ro.ubb.topics.repository;

import ro.ubb.topics.model.Topic;

public interface TopicRepository extends MainRepository<Topic, Long>{

    Topic findByTopicname(String name);
}
