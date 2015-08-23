package org.podcastpedia.core.user;

import org.podcastpedia.common.domain.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    public static final int USER_NOT_YET_ENABLED = 0;
    @Autowired
	UserDao userDao;

	@Override
	public List<Podcast> getSubscriptions(String username) {
		return userDao.getSubscriptions(username);
	}

	@Override
	public List<Episode> getLatestEpisodesFromSubscriptions(String username) {
		return userDao.getLatestEpisodesFromSubscriptions(username);
	}

    @Override
    public void submitUserForRegistration(User user) {
        user.setRegistrationDate(new Date());
        user.setEnabled(USER_NOT_YET_ENABLED);
        //if display name not introduced then use the name from the email address(the one before @)
        if(user.getDisplayName()==null){
            user.setDisplayName(user.getUsername());
        }
        userDao.addUser(user);
    }

    @Override
    public boolean isExistingUser(String username) {
        User user = userDao.selectUserByUsername(username);

        return user != null;
    }

    @Override
    public void subscribeToPodcast(String username, int podcastId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("email", username);
        params.put("podcastId", podcastId);

        userDao.subscribeToPodcast(params);
    }

    @Override
    public void votePodcast(String username, int podcastId, int vote) {
        PodcastVote podcastVote = new PodcastVote();
        podcastVote.setUsername(username);
        podcastVote.setPodcastId(podcastId);
        podcastVote.setVote(vote);

        userDao.addPodcastVote(podcastVote);
    }

    @Override
    public void voteEpisode(String username, int podcastId, int episodeId, int vote) {
        EpisodeVote episodeVote = new EpisodeVote();
        episodeVote.setUsername(username);
        episodeVote.setPodcastId(podcastId);
        episodeVote.setEpisodeId(episodeId);
        episodeVote.setVote(vote);

        userDao.addEpisodeVote(episodeVote);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
