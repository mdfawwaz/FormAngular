package com.prodapt.learningspring.controller;

import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.prodapt.learningspring.controller.binding.AddCommentForm;

import com.prodapt.learningspring.controller.binding.AddPostForm;
import org.springframework.web.bind.annotation.CrossOrigin;


import com.prodapt.learningspring.controller.exception.ResourceNotFoundException;

import com.prodapt.learningspring.entity.LikeRecord;
import com.prodapt.learningspring.entity.MutedAuthor;
import com.prodapt.learningspring.entity.Comment;
import com.prodapt.learningspring.entity.FavPost;
import com.prodapt.learningspring.entity.FavoriteAuthor;
import com.prodapt.learningspring.entity.LikeId;

import com.prodapt.learningspring.entity.Post;

import com.prodapt.learningspring.entity.User;

import com.prodapt.learningspring.model.RegistrationForm;

import com.prodapt.learningspring.repository.CommentRepository;
import com.prodapt.learningspring.repository.FavPostRepository;
import com.prodapt.learningspring.repository.FavoriteAuthorRepository;
import com.prodapt.learningspring.repository.LikeCRUDRepository;

import com.prodapt.learningspring.repository.LikeCountRepository;
import com.prodapt.learningspring.repository.MutedAuthorRepository;
import com.prodapt.learningspring.repository.PostRepository;

import com.prodapt.learningspring.repository.UserRepository;

import com.prodapt.learningspring.service.DomainUserService;
import com.prodapt.learningspring.service.FavPostMuteService;

import jakarta.annotation.PostConstruct;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/forum")
@CrossOrigin(origins = {"http://localhost:38955"})

public class UserProfileController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private DomainUserService domainUserService;
	@Autowired
	private LikeCRUDRepository likeCRUDRepository;

	@Autowired
	private FavPostRepository favPostRepository;
	
	@Autowired
	private FavPostMuteService favMutePostService;  

	private List<User> userList;
	private List<Comment> commentList;
	
	  @Value("${upload.directory}")
	  private String uploadDirectory;
	  

	  

	@PostConstruct
	public void init() {
		userList = new ArrayList<>();
		commentList = new ArrayList<>();
	}

	@GetMapping("/post/form")
		@ResponseBody // This annotation tells Spring to treat the return value as the response body (JSON)

	public String getPostForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		AddPostForm postForm = new AddPostForm();
		User author = domainUserService.getByName(userDetails.getUsername()).get();
		postForm.setUserId(author.getId());
		model.addAttribute("postForm", postForm);
		return "forum/postForm";
	}

	@PostMapping("/post/add")
	public String addNewPost(@ModelAttribute("postForm") AddPostForm postForm, BindingResult bindingResult,
			RedirectAttributes attr) throws ServletException {
		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult.getFieldErrors());
			attr.addFlashAttribute("org.springframework.validation.BindingResult.post", bindingResult);
			attr.addFlashAttribute("post", postForm);
			return "redirect:/forum/post/form";
		}

		Optional<User> user = userRepository.findById(postForm.getUserId());
		if (user.isEmpty()) {
			throw new ServletException("Something went seriously wrong and we couldn't find the user in the DB");
		}
		Post post = new Post();
		post.setAuthor(user.get());
		post.setTimestamp(LocalDateTime.now());
		post.setContent(postForm.getContent());
		postRepository.save(post);
		return String.format("redirect:/forum/post/%d", post.getId());
	}

	@GetMapping("/post/{id}")
	@ResponseBody // This annotation tells Spring to treat the return value as the response body (JSON)

	public Optional<Post> postDetail(@PathVariable int id, Model model)
			throws ResourceNotFoundException {
		Optional<Post> post = postRepository.findById(id);
		if (post.isEmpty()) {
			throw new ResourceNotFoundException("No post with the requested ID");
		}
		model.addAttribute("post", post.get());
		//model.addAttribute("likerName", userDetails.getUsername());
		//model.addAttribute("commenterName", userDetails.getUsername());
		commentList = commentRepository.findAllByPostId(id);
		model.addAttribute("commentList", commentList);

		int numLikes = likeCRUDRepository.countByLikeIdPost(post.get());
		model.addAttribute("likeCount", numLikes);
		return post;
	}

	@PostMapping("/post/{id}/like")
	public String postLike(@PathVariable int id, String likerName, RedirectAttributes attr) {
		LikeId likeId = new LikeId();
		likeId.setUser(userRepository.findByName(likerName).get());
		likeId.setPost(postRepository.findById(id).get());
		LikeRecord like = new LikeRecord();
		like.setLikeId(likeId);
		likeCRUDRepository.save(like);
		return String.format("redirect:/forum/post/%d", id);
	}

	@GetMapping("/register")
	public String getRegistrationForm(Model model) {
		if (!model.containsAttribute("registrationForm")) {
			model.addAttribute("registrationForm", new RegistrationForm());
		}
		return "forum/register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute("registrationForm") RegistrationForm registrationForm,
			BindingResult bindingResult, RedirectAttributes attr) {
		if (bindingResult.hasErrors()) {
			attr.addFlashAttribute("org.springframework.validation.BindingResult.registrationForm", bindingResult);
			attr.addFlashAttribute("registrationForm", registrationForm);
			return "redirect:/register";
		}
		if (!registrationForm.isValid()) {
			attr.addFlashAttribute("message", "Passwords must match");
			attr.addFlashAttribute("registrationForm", registrationForm);
			return "redirect:/register";
		}

		System.out.println(domainUserService.save(registrationForm.getUsername(), registrationForm.getPassword(),
				registrationForm.getBio(), registrationForm.getDateOfBirth(), registrationForm.getEmail()));
		attr.addFlashAttribute("result", "Registration success!");
		return "redirect:/login";
	}

	@PostMapping("/post/{id}/comment")
	public String addCommentToPost(String commenterName, HttpServletRequest request, @PathVariable int id) {
		String content = request.getParameter("content");
		Optional<User> user = userRepository.findByName(commenterName);
		Optional<Post> post = postRepository.findById(id);
		if (user.isPresent() && post.isPresent()) {
			Comment comment = new Comment();
			comment.setUser(user.get());
			comment.setPost(post.get());
			comment.setContent(content);
			commentRepository.save(comment);
			return String.format("redirect:/forum/post/%d", id);
		}

		return "redirect:/forum/post/error";

	}

	@Autowired
	private FavoriteAuthorRepository favoriteAuthorRepository;

	@Autowired
	private MutedAuthorRepository mutedAuthorRepository;

	@GetMapping("/userProfile")
	public String userProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		Optional<User> user = userRepository.findByName(userDetails.getUsername());

		if (user.isPresent()) {
			User currentUser = user.get();
			List<User> authors = new ArrayList<>();
			userRepository.findAll().forEach(newuser -> authors.add(newuser));


			// Fetch and display the posts authored by the logged-in user
			List<Post> userPosts = postRepository.findAllByAuthorId(currentUser.getId());

			model.addAttribute("id", currentUser.getId());
			model.addAttribute("username", currentUser.getName());
			model.addAttribute("email", currentUser.getEmail());
			model.addAttribute("dateOfBirth", currentUser.getDateOfBirth());
			model.addAttribute("bio", currentUser.getBio());
			
			model.addAttribute("commenterName" , userDetails.getUsername());

			model.addAttribute("authors", authors);
			model.addAttribute("posts", userPosts); // Add the user's posts to the model
			
			model.addAttribute("user", user.orElse(null)); // user.orElse(null) handles the case when user is not present


			return "forum/userProfile";
		} else {
			// Handle the case when the user is not found
			return "redirect:/"; // Redirect to the homepage or handle it accordingly
		}
	}
	@PostMapping("/user/upload-profile-picture")
	public String postPic(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails userDetails) {
	    // Retrieve the current user
	    Optional<User> userOptional = userRepository.findByName(userDetails.getUsername());

	    if (userOptional.isPresent()) {
	        User user = userOptional.get();
	        try {
	            String fileName = file.getOriginalFilename();
	            File directory = new File(uploadDirectory);

	            if (!directory.exists()) {
	                directory.mkdirs();
	            }

	            Path filePath = Paths.get(uploadDirectory, fileName);

	            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	            // Update the user's imagePath
	            user.setImagePath(fileName);

	            // Save the updated user
	            userRepository.save(user);

	            System.out.println(user.getImagePath());
	        } catch (IOException e) {
	            // Handle the error appropriately, e.g., show an error message to the user
	            return "uploadError";
	        }
	    } else {
	        // Handle the case when the user is not found
	        return "redirect:/forum/userProfile"; // Redirect to the homepage or handle it accordingly
	    }

	    return "redirect:/forum/userProfile";
	}


	@GetMapping("/user/markFavorite")
	public String listOfFavoriteAuthors(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		Optional<User> user = userRepository.findByName(userDetails.getUsername());
		List<User> favoriteAuthors = new ArrayList<>();
		List<FavoriteAuthor> favorites = favoriteAuthorRepository.findAllByUserId(user.get().getId());
		for (FavoriteAuthor f : favorites) {
			User newUser = userRepository.findById(f.getAuthor().getId()).get();
			favoriteAuthors.add(newUser);
		}
		model.addAttribute("favoriteAuthors", favoriteAuthors);
		return "forum/markFavorite";
	}

	@GetMapping("/user/markMuted")
	public String listAllMutedAuthors(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		Optional<User> user = userRepository.findByName(userDetails.getUsername());

		if (user.isPresent()) {
			User currentUser = user.get();
			List<MutedAuthor> muted = mutedAuthorRepository.findAllByUserId(user.get().getId());
			List<User> mutedAuthors = new ArrayList<>();

			for (MutedAuthor m : muted) {
				User newUser = userRepository.findById(m.getAuthor().getId()).get();

				// Check if the author is also a favorite
				if (!isAuthorFavorite(currentUser, newUser)) {
					mutedAuthors.add(newUser);
				}
			}

			model.addAttribute("mutedAuthors", mutedAuthors);
		}

		return "forum/mutedAuthors";
	}

	@PostMapping("/user/markFavorite")
	public String markAuthorAsFavorite(@RequestParam("authorId") int authorId,
			@AuthenticationPrincipal UserDetails userDetails, Model model) {
		Optional<User> user = userRepository.findByName(userDetails.getUsername());

		if (user.isPresent()) {
			User currentUser = user.get();
			Optional<User> author = userRepository.findById(authorId);

			if (author.isPresent()) {
				User favoriteAuthor = author.get();

				// Check if the author is already a favorite
				if (!isAuthorFavorite(currentUser, favoriteAuthor)) {
					System.out.println("Here");
					FavoriteAuthor favorite = new FavoriteAuthor();
					favorite.setUser(currentUser);
					favorite.setAuthor(favoriteAuthor);
					String name = favorite.getUser().getName();
					model.addAttribute("favoriteAuthors", name);
					favoriteAuthorRepository.save(favorite);
				}
			}
		}

		return "redirect:/forum/user/markFavorite";
	}

	private boolean isAuthorFavorite(User currentUser, User author) {
		List<FavoriteAuthor> favorites = favoriteAuthorRepository.findByUserAndAuthor(currentUser, author);
		return !favorites.isEmpty();
	}

	@PostMapping("/user/markMuted")
	public String markAuthorAsMuted(@RequestParam("authorId") int authorId,
			@AuthenticationPrincipal UserDetails userDetails) {
		Optional<User> user = userRepository.findByName(userDetails.getUsername());

		if (user.isPresent()) {
			User currentUser = user.get();
			Optional<User> author = userRepository.findById(authorId);

			if (author.isPresent()) {
				User mutedAuthor = author.get();

				// Check if the author is already muted
				if (!isAuthorMuted(currentUser, mutedAuthor)) {
					MutedAuthor muted = new MutedAuthor();
					muted.setUser(currentUser);
					muted.setAuthor(mutedAuthor);
					mutedAuthorRepository.save(muted);
				}
			}
		}
		return "redirect:/forum/user/markMuted";
	}

	private boolean isAuthorMuted(User currentUser, User author) {
		List<MutedAuthor> mutedAuthors = mutedAuthorRepository.findByUserAndAuthor(currentUser, author);
		return !mutedAuthors.isEmpty();
	}

	@Transactional
	@PostMapping("/user/removeFavorite")
	public String removeFavoriteAuthor(@RequestParam("authorId") int authorId,
			@AuthenticationPrincipal UserDetails userDetails) {
		Optional<User> user = userRepository.findByName(userDetails.getUsername());
		System.out.println("going...");

		if (user.isPresent()) {
			User currentUser = user.get();
			Optional<User> author = userRepository.findById(authorId);

			if (author.isPresent()) {
				User favoriteAuthor = author.get();
				System.out.println(author);
				// Check if the author is a favorite
				if (isAuthorFavorite(currentUser, favoriteAuthor)) {
					// Remove the favorite
					favoriteAuthorRepository.deleteByUserAndAuthor(currentUser, favoriteAuthor);
//	                    favoriteAuthorRepository.save(del);

				}
			}
		}
		return "redirect:/forum/user/markFavorite";
	}

	@Transactional
	@PostMapping("/user/removeMuted")
	public String removeMutedAuthor(@RequestParam("authorId") int authorId,
			@AuthenticationPrincipal UserDetails userDetails) {
		Optional<User> user = userRepository.findByName(userDetails.getUsername());
		System.out.println("going...");

		if (user.isPresent()) {
			User currentUser = user.get();
			Optional<User> author = userRepository.findById(authorId);

			if (author.isPresent()) {
				User mutedAuthor = author.get();
				System.out.println(author);
				// Check if the author is a favorite
				if (isAuthorMuted(currentUser, mutedAuthor)) {
					// Remove the favorite
					mutedAuthorRepository.deleteByUserAndAuthor(currentUser, mutedAuthor);
//	                    favoriteAuthorRepository.save(del);

				}
			}
		}
		return "redirect:/forum/user/markMuted";
	}
	@GetMapping("/goEdit")
	public String goToEdit() {
		return "redirect:/forum/editProfile";
	}

	@Transactional
	@GetMapping("/editProfile")
	public String editProfile(Model model) {
		model.addAttribute("registrationForm", new RegistrationForm());
		return "forum/editprofile";
	}
	@Transactional
	     @PostMapping("/editProfile")
	     public String postProfileDetails(@ModelAttribute("userForm") RegistrationForm registrationForm,@AuthenticationPrincipal UserDetails userDetails,BindingResult bindingResult,RedirectAttributes attr) {
	    	 if (bindingResult.hasErrors()) {
	 			attr.addFlashAttribute("org.springframework.validation.BindingResult.registrationForm", bindingResult);
	 			attr.addFlashAttribute("registrationForm", registrationForm);
	 			return "redirect:/forum/editProfile";
	 		}
	    	 String username = userDetails.getUsername();
	    	 User user = domainUserService.getByName(username).get();
	    	 domainUserService.update(registrationForm.getUsername(), registrationForm.getBio(),registrationForm.getDateOfBirth(),user.getEmail());
	    	 return "redirect:/forum/userProfile";
	     }

	// adding post to favorite
	@PostMapping("/post/{id}/fav")
	public String AddfavPost(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails,
			RedirectAttributes redirectAttributes) {
		Optional<User> user = userRepository.findByName(userDetails.getUsername());
		Optional<Post> post = postRepository.findById(id);
		System.out.println(id);
		System.out.println(user);
		if (user.isPresent() && post.isPresent()) {

			if (favPostRepository.existsByUserAndPost(user.get(), post.get())) {
				return "redirect:/forum/userProfile";
			} else {
				FavPost favPost = new FavPost();
				favPost.setPost(post.get());
				favPost.setUser(user.get());
				favPostRepository.save(favPost);
//				redirectAttributes.addFlashAttribute("FavMessage", "post added to favorites");
				return "redirect:/forum/userProfile";
			}
		}
		return "redirect:/forum/post/error";
	}

	@GetMapping("/post/favfeed")
	public String favpostfeed(Model model, @AuthenticationPrincipal UserDetails userDetails)
			throws ResourceNotFoundException {
		Optional<User> user = userRepository.findByName(userDetails.getUsername());
		favMutePostService.favpostList = favMutePostService.findAllFavPostsByUser(user.get());
		model.addAttribute("favpostList", favMutePostService.favpostList);
		model.addAttribute("commenterName", userDetails.getUsername());
		return "forum/favPost";
	}
}
